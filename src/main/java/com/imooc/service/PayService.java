package com.imooc.service;

import com.imooc.dto.OrderDTO;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundResponse;

/**
 * @author: icych
 * @description: 支付
 * @date: Created on 23:44 2018/7/17
 */
public interface PayService {

    /**
     * 微信支付 -- 发起支付
     *
     * @param orderDTO 包含订单id与openid
     * @return 预下单结果
     */
    PayResponse create(OrderDTO orderDTO);

    /**
     * 微信支付 -- 处理微信异步通知
     *
     * @param notifyData 微信推送的支付结果通知
     * @return
     */
    PayResponse notify(String notifyData);

    /**
     * 微信退款
     *
     * @param orderDTO
     * @return
     */
    RefundResponse refund(OrderDTO orderDTO);
}
