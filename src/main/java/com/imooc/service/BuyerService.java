package com.imooc.service;

import com.imooc.dto.OrderDTO;

/**
 * @author: icych
 * @description: 买家
 * @date: Created on 22:10 2018/7/16
 */
public interface BuyerService {

    /**
     * 查询订单
     * @param openid
     * @param orderId
     * @return
     */
    OrderDTO findOrderOne(String openid, String orderId);

    /**
     * 取消订单
     * @param openid
     * @param orderId
     * @return
     */
    OrderDTO cancelOrder(String openid, String orderId);
}
