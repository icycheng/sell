package com.imooc.controller;

import com.imooc.dto.OrderDTO;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.service.OrderService;
import com.imooc.service.PayService;
import com.lly835.bestpay.model.PayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * @author: icych
 * @description: 支付
 * @date: Created on 21:32 2018/7/17
 */
@Controller
@RequestMapping("/pay")
@Slf4j
public class PayController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PayService payService;

    @GetMapping("/create")
    public ModelAndView create(@RequestParam("orderId") String orderId,//
                               @RequestParam("returnUrl") String returnUrl,//
                               Map<String, Object> map) {

        //1.查询订单
        OrderDTO orderDTO = orderService.findOne(orderId);
        if (orderDTO == null) {
            log.error("[发起微信支付] 订单不存在, orderID={}", orderId);
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        //2.发起支付
        PayResponse payResponse;
        try {
            payResponse = payService.create(orderDTO);
        } catch (Exception e) {
            log.error("[发起微信支付] 支付异常", e);
            return null;
        }
        map.put("payResponse", payResponse);
        try {
            String decodeUrl = URLDecoder.decode(returnUrl, "UTF-8");
            map.put("returnUrl", decodeUrl);
        } catch (UnsupportedEncodingException e) {
            log.error("[微信支付] 解析返回地址错误, returnUrl={}", returnUrl);
        }
//        map.put("returnUrl", returnUrl);
        return new ModelAndView("pay/create", map);
    }

    @PostMapping("/notify")
    public ModelAndView notify(@RequestBody String notifyData) {
        payService.notify(notifyData);

        //返回给微信处理结果
        return new ModelAndView("pay/success");
    }

}
