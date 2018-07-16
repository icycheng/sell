package com.imooc.repository;

import com.imooc.BaseTest;
import com.imooc.dataobject.OrderMaster;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;

/**
 * @author: icych
 * @description:
 * @date: Created on 17:47 2018/7/15
 */
public class OrderMasterRepositoryTest extends BaseTest {
    @Autowired
    private OrderMasterRepository repository;

    private static final String OPENID = "110110";

    @Test
    public void save() {
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setOrderId("1234589");
        orderMaster.setBuyerName("师弟");
        orderMaster.setBuyerPhone("17655556666");
        orderMaster.setBuyerAddress("幕课网");
        orderMaster.setBuyerOpenid(OPENID);
        orderMaster.setOrderAmount(new BigDecimal(3.8));

        Object result = repository.save(orderMaster);
        Assert.assertNotNull(result);
    }

    @Test
    public void findByBuyerOpenid() {
        PageRequest request = new PageRequest(1, 2);
        Page<OrderMaster> page = repository.findByBuyerOpenid(OPENID, request);
        System.out.println(page.getTotalElements());
        System.out.println(page.getTotalPages());
    }
}