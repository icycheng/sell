package com.imooc.controller;

import com.imooc.converter.OrderForm2OrderDTOConverter;
import com.imooc.dto.OrderDTO;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.form.OrderForm;
import com.imooc.service.BuyerService;
import com.imooc.service.OrderService;
import com.imooc.util.ResultVOUtil;
import com.imooc.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: icych
 * @description: 买家订单Controller
 * @date: Created on 0:44 2018/7/16
 */
@SuppressWarnings("unchecked")
@RestController
@RequestMapping("/buyer/order")
@Slf4j
public class BuyerOrderConroller {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BuyerService buyerService;

    //创建订单
    @PostMapping("/create")
    public ResultVO<Map<String, Object>> create(@Valid OrderForm orderForm,//
                                                BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.error("[创建订单] 参数错误, orderForm={}", orderForm);
            throw new SellException(ResultEnum.ILEGAL_ARGUMENT.getCode(), //
                    bindingResult.getFieldError().getDefaultMessage());
        }

        OrderDTO orderDTO = OrderForm2OrderDTOConverter.convert(orderForm);
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            log.error("[创建订单] 参数错误: 购物车不能为空");
            throw new SellException(ResultEnum.CART_EMPTY);
        }
        OrderDTO createResult = orderService.create(orderDTO);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("orderId", createResult.getOrderId());

        return ResultVOUtil.success(resultMap);
    }

    //订单列表
    @GetMapping("/list")
    public ResultVO<List<OrderDTO>> list(@RequestParam("openid") String openid, //
                                         @RequestParam(value = "page", defaultValue = "0") Integer page, //
                                         @RequestParam(value = "size", defaultValue = "5") Integer size) {

        if (StringUtils.isEmpty(openid)) {
            log.error("[查询订单列表] 用户openid为空");
            throw new SellException(ResultEnum.ILEGAL_ARGUMENT);
        }

        PageRequest request = new PageRequest(page, size);
        Page<OrderDTO> orderDTOPage = orderService.findList(openid, request);

        return ResultVOUtil.success(orderDTOPage.getContent());
    }

    //订单详情
    @GetMapping("/detail")
    public ResultVO<OrderDTO> detail(@RequestParam(value = "openid") String openid, //
                                     @RequestParam(value = "orderId") String orderId) {

        //有安全漏洞
        // OrderDTO orderDTO = orderService.findOne(orderId);

        //避免横向越权
        OrderDTO orderDTO = buyerService.findOrderOne(openid, orderId);
        return ResultVOUtil.success(orderDTO);
    }

    //取消订单
    @PostMapping("/cancel")
    public ResultVO<OrderDTO> cancel(@RequestParam(value = "openid") String openid, //
                                     @RequestParam(value = "orderId") String orderId) {

        //有安全漏洞
        //OrderDTO orderDTO = orderService.findOne(orderId);
        //OrderDTO cancelResult = orderService.cancel(orderDTO);

        //避免横向越权
        buyerService.cancelOrder(openid, orderId);
        return ResultVOUtil.success();
    }

}
