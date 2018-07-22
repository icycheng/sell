package com.imooc.service.impl;

import com.imooc.dto.OrderDTO;
import com.imooc.enums.PayStatusEnum;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.service.OrderService;
import com.imooc.service.PayService;
import com.imooc.util.JsonUtil;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundRequest;
import com.lly835.bestpay.model.RefundResponse;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author: icych
 * @description: 支付
 * @date: Created on 23:45 2018/7/17
 */
@Service
@Slf4j
public class PayServiceImpl implements PayService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BestPayServiceImpl payService;

    @Override
    public PayResponse create(OrderDTO orderDTO) {
        //1.配置WxPayH5Config

        //2.发起支付
        PayRequest payRequest = new PayRequest();
        payRequest.setOpenid(orderDTO.getBuyerOpenid());
        payRequest.setOrderAmount(orderDTO.getOrderAmount().doubleValue());
        payRequest.setOrderName("微信点餐订单");
        payRequest.setOrderId(orderDTO.getOrderId());
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);

        log.info("[微信支付] 发起支付\npayRequest={}", JsonUtil.toJson(payRequest));
        PayResponse response = payService.pay(payRequest);
        log.info("[微信支付] 发起支付]\npayResponse={}", JsonUtil.toJson(response));
        return response;
    }

    @Override
    public PayResponse notify(String notifyData) {
        //1.验证签名
        //2.支付状态
        //3.支付金额
        //4.支付人(下单人 == 支付人?)

        PayResponse payResponse = payService.asyncNotify(notifyData);
        log.info("\n[微信支付] 异步通知, payResponse={}", JsonUtil.toJson(payResponse));

        //查询订单
        String orderId = payResponse.getOrderId();
        OrderDTO orderDTO = orderService.findOne(orderId);
        if (orderDTO == null) {
            log.error("[微信支付] 异步通知, 订单不存在, orderId={}", orderId);
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        //如果订单支付状态已经正确处理过,直接返回
        if (orderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())) {
            return payResponse;
        }

        //判断金额是否一致
        Double payResponseOrderAmount = payResponse.getOrderAmount();
        BigDecimal orderDTOOrderAmount = orderDTO.getOrderAmount();
        if (!orderDTOOrderAmount.toString().equals(payResponseOrderAmount.toString())) {
            log.error("[微信支付] 异步通知, 订单金额不一致, orderId={}, 微信通知金额={}, 系统订单金额={}",//
                    orderId, payResponse.getOrderAmount(), orderDTOOrderAmount);
            throw new SellException(ResultEnum.WXPAY_NOTIFY_MONEY_VERIFY_ERROR);
        }

        //修改订单支付状态
        orderService.paid(orderDTO);
        return payResponse;
    }

    @Override
    public RefundResponse refund(OrderDTO orderDTO) {
        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setOrderId(orderDTO.getOrderId());
        refundRequest.setOrderAmount(orderDTO.getOrderAmount().doubleValue());
        refundRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        log.info("\n[微信退款] refundRequest={}", JsonUtil.toJson(refundRequest));

        RefundResponse refundResponse = payService.refund(refundRequest);
        log.info("\n[微信退款] refundResponse={}", JsonUtil.toJson(refundResponse));
        return refundResponse;
    }
}
