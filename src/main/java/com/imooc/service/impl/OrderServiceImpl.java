package com.imooc.service.impl;

import com.imooc.converter.OrderMaster2OrderDTOConverter;
import com.imooc.dataobject.OrderDetail;
import com.imooc.dataobject.OrderMaster;
import com.imooc.dataobject.ProductInfo;
import com.imooc.dto.CartDTO;
import com.imooc.dto.OrderDTO;
import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.PayStatusEnum;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.repository.OrderDetailRepository;
import com.imooc.repository.OrderMasterRepository;
import com.imooc.service.*;
import com.imooc.util.BigDecimalUtil;
import com.imooc.util.KeyGenerator;
import com.lly835.bestpay.model.RefundResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: icych
 * @description: 订单
 * @date: Created on 19:22 2018/7/15
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private PayService payService;

    @Autowired
    private PushMessageService pushMessageService;

    @Autowired
    private WebSocket webSocket;

    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {

        //生成订单id
        String orderId = KeyGenerator.generate();

        BigDecimal orderAmount = new BigDecimal("0");

        List<CartDTO> cartDTOList = new ArrayList<>();//多件商品(商品编号+购买数量)

        //1.查询商品(数量,价格)
        for (OrderDetail orderDetail : orderDTO.getOrderDetailList()) {
            ProductInfo productInfo = productService.findOne(orderDetail.getProductId());
            if (productInfo == null) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            //2.计算订单总价
            BigDecimal perProductAmount = BigDecimalUtil.mul(productInfo.getProductPrice().doubleValue(),//
                    orderDetail.getProductQuantity().doubleValue());
            orderAmount = BigDecimalUtil.add(orderAmount.doubleValue(), perProductAmount.doubleValue());

            //订单详情入库
            orderDetail.setDetailId(KeyGenerator.generate());
            orderDetail.setOrderId(orderId);
            BeanUtils.copyProperties(productInfo, orderDetail);
            orderDetailRepository.save(orderDetail);

            CartDTO cartDTO = new CartDTO(productInfo.getProductId(), orderDetail.getProductQuantity());
            cartDTOList.add(cartDTO);
        }

        //3.订单入库

        orderDTO.setOrderId(orderId);

        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setOrderAmount(orderAmount);
        //!!拷贝属性注意忽略orderDTO的null值
        BeanUtils.copyProperties(orderDTO, orderMaster, "orderAmount", "orderStatus", "payStatus");

        orderMasterRepository.save(orderMaster);

        //4.扣库存
        productService.decreaseStock(cartDTOList);

        //5.推送WebSocket消息到卖家后台
        webSocket.send("收到新订单, 订单号: " + orderDTO.getOrderId());

        return orderDTO;
    }

    @Override
    public OrderDTO findOne(String orderId) {
        OrderMaster orderMaster = orderMasterRepository.findOne(orderId);
        if (orderMaster == null) {
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        List<OrderDetail> orderDetailList = //
                orderDetailRepository.findByOrderId(orderMaster.getOrderId());
        if (CollectionUtils.isEmpty(orderDetailList)) {
            throw new SellException(ResultEnum.ORDER_DETAIL_NOT_EXIST);
        }
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(String buyerOpenid, Pageable pageable) {
        Page<OrderMaster> orderMasterPage = //
                orderMasterRepository.findByBuyerOpenid(buyerOpenid, pageable);
        List<OrderMaster> orderMasterList = orderMasterPage.getContent();
        //OrderMasterList ==> orderDTOList
        List<OrderDTO> orderDTOList = OrderMaster2OrderDTOConverter.convert(orderMasterList);

        //orderMasterPage ==> orderDTOPage
        return new PageImpl<>(orderDTOList, pageable, orderMasterPage.getTotalElements());
    }

    @Override
    @Transactional
    public OrderDTO cancel(OrderDTO orderDTO) {

        //1.判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("[取消订单] 订单状态不正确, orderId={}, orderStatus={}", //
                    orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //2.修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.CANCELED.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);

        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if (updateResult == null) {
            log.error("[取消订单] 订单状态更新失败, orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_STATUS_UPDATE_FAILED);
        }
        //orderDTO.setUpdateTime(new Date());

        //3.返还库存
        List<OrderDetail> orderDetailList = orderDTO.getOrderDetailList();
        if (CollectionUtils.isEmpty(orderDetailList)) {
            log.error("[取消订单] 订单中无商品详情, orderDTO={}", orderDTO);
            throw new SellException(ResultEnum.ORDER_STATUS_UPDATE_FAILED);
        }

        //orderDetailList ==> cartDTOList
        List<CartDTO> cartDTOList = orderDetailList.stream().
                map(e -> new CartDTO(e.getProductId(), e.getProductQuantity())) //
                .collect(Collectors.toList());
        productService.increaseStock(cartDTOList);

        //4.如果已支付,需要退款
        if (orderMaster.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())) {
            //退款相关
            RefundResponse refundResponse = payService.refund(orderDTO);
            if (refundResponse.getOutRefundNo() != null) {
                orderMaster.setOrderStatus(PayStatusEnum.REFUNDED.getCode());
                updateResult = orderMasterRepository.save(orderMaster);
                if (updateResult == null) {
                    log.error("[取消订单] 订单支付状态更新失败, orderMaster={}", orderMaster);
                    throw new SellException(ResultEnum.PAY_STATUS_UPDATE_FAILED);
                }
                orderDTO.setUpdateTime(new Date());
            }
        }
        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO finish(OrderDTO orderDTO) {
        //1.判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode()) //
                || !orderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())) {
            log.error("[完结订单] 订单状态不正确, orderId={}, orderStatus={}", //
                    orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //2.修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if (updateResult == null) {
            log.error("[完结订单] 订单状态更新失败, orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_STATUS_UPDATE_FAILED);
        }
        orderDTO.setUpdateTime(new Date());

        //推送模板消息
        //pushMessageService.push(orderDTO);

        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO paid(OrderDTO orderDTO) {
        //1.判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("[订单支付完成] 订单状态不正确, orderId={}, orderStatus={}", //
                    orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //2.判断支付状态
        if (!orderDTO.getOrderStatus().equals(PayStatusEnum.WAIT.getCode())) {
            log.error("[订单支付完成] 订单支付状态不正确, orderId={}, orderStatus={}", //
                    orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.PAY_STATUS_ERROR);
        }

        //修改支付状态
        orderDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);

        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if (updateResult == null) {
            log.error("[订单支付完成] 订单支付状态更新失败, orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.PAY_STATUS_UPDATE_FAILED);
        }
        orderDTO.setUpdateTime(new Date());

        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(Pageable pageable) {
        Page<OrderMaster> orderMasterPage = //
                orderMasterRepository.findAll(pageable);
        List<OrderMaster> orderMasterList = orderMasterPage.getContent();
        //OrderMasterList ==> orderDTOList
        List<OrderDTO> orderDTOList = OrderMaster2OrderDTOConverter.convert(orderMasterList);

        //orderMasterPage ==> orderDTOPage
        return new PageImpl<>(orderDTOList, pageable, orderMasterPage.getTotalElements());
    }
}
