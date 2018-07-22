package com.imooc.service;

import com.imooc.dto.OrderDTO;

/**
 * @author: icych
 * @description: 微信模板消息推送
 * @date: Created on 21:18 2018/7/21
 */
public interface PushMessageService {

    void push(OrderDTO orderDTO);

}
