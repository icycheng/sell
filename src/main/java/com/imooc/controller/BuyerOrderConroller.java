package com.imooc.controller;

import com.imooc.converter.OrderForm2OrderDTOConverter;
import com.imooc.dto.OrderDTO;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.form.OrderForm;
import com.imooc.service.OrderService;
import com.imooc.util.ResultVOUtil;
import com.imooc.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: icych
 * @description:
 * @date: Created on 0:44 2018/7/16
 */
@RestController
@RequestMapping("/buyer/order")
@Slf4j
public class BuyerOrderConroller {

    @Autowired
    private OrderService orderService;

    @SuppressWarnings("unchecked")
    //创建订单
    @PostMapping
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

    //订单详情

    //取消订单


}
