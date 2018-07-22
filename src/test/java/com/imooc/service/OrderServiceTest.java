package com.imooc.service;

import com.imooc.BaseTest;
import com.imooc.dataobject.OrderDetail;
import com.imooc.dto.OrderDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: icych
 * @description:
 * @date: Created on 20:21 2018/7/15
 */
@Slf4j
public class OrderServiceTest extends BaseTest {

    @Autowired
    private OrderService orderService;

    private static final String BUYER_OPENID = "110110";

    @Test
    public void create() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerName("廖师兄");
        orderDTO.setBuyerAddress("慕课网");
        orderDTO.setBuyerPhone("13323332333");
        orderDTO.setBuyerOpenid(BUYER_OPENID);

        //购物车
        List<OrderDetail> orderDetailList = new ArrayList<>();
        OrderDetail orderDetail = new OrderDetail();
        OrderDetail orderDetail2 = new OrderDetail();

        orderDetail.setProductId("123458");
        orderDetail.setProductQuantity(1);

        orderDetail2.setProductId("123459");
        orderDetail2.setProductQuantity(2);

        orderDetailList.add(orderDetail);
        orderDetailList.add(orderDetail2);

        orderDTO.setOrderDetailList(orderDetailList);
        OrderDTO result = orderService.create(orderDTO);
        log.info("[创建订单] result = {}", result);
        Assert.assertNotNull(result);
    }

    @Test
    public void findOne() {
        OrderDTO result = orderService.findOne("1531660019059309749");
        log.info("查询到订单: {}", result);
        Assert.assertNotNull(result);
    }

    @Test
    public void findList() {
        PageRequest request = new PageRequest(0, 2);
        Page<OrderDTO> result = orderService.findList(BUYER_OPENID, request);
        Assert.assertEquals(2, result.getSize());
    }

    @Test
    public void cancel() {
        OrderDTO orderDTO = orderService.findOne("1531660019059309749");
        OrderDTO result = orderService.cancel(orderDTO);
        Assert.assertEquals(Integer.valueOf(2), result.getOrderStatus());
    }

    @Test
    public void finish() {
        OrderDTO orderDTO = orderService.findOne("1531659571817285887");
        OrderDTO result = orderService.finish(orderDTO);
        Assert.assertEquals(Integer.valueOf(1), result.getOrderStatus());
    }

    @Test
    public void paid() {
        OrderDTO orderDTO = orderService.findOne("1531659157909232033");
        OrderDTO result = orderService.paid(orderDTO);
        Assert.assertEquals(Integer.valueOf(1), result.getPayStatus());
    }

    @Test
    public void findListAll() {
        PageRequest request = new PageRequest(2, 5);
        Page<OrderDTO> result = orderService.findList(request);
//        Assert.assertEquals(5, result.getSize());
        Assert.assertTrue("查询所有订单列表", result.getTotalElements() > 0);
    }
}