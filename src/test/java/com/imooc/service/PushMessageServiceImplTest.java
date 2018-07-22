package com.imooc.service;

import com.imooc.BaseTest;
import com.imooc.dto.OrderDTO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: icych
 * @description:
 * @date: Created on 21:33 2018/7/21
 */
public class PushMessageServiceImplTest extends BaseTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PushMessageService service;

    @Test
    public void push() {
        OrderDTO orderDTO = orderService.findOne("1532012017458999305");
        service.push(orderDTO);
    }
}