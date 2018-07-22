package com.imooc.service;

import com.imooc.BaseTest;
import com.imooc.dto.OrderDTO;
import com.imooc.util.JsonUtil;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundResponse;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: icych
 * @description:
 * @date: Created on 0:13 2018/7/18
 */
@Slf4j
public class PayServiceTest extends BaseTest {

    private static final String ORDER_NAME = "微信点餐订单";

    @Autowired
    private BestPayServiceImpl bestPayService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PayService payService;

    @Test
    public void create() {
        //BestPayServiceImpl payService = new BestPayServiceImpl();
        OrderDTO orderDTO = orderService.findOne("1531749702764695495");

        PayRequest payRequest = new PayRequest();
        payRequest.setOpenid(orderDTO.getBuyerOpenid());
        payRequest.setOrderAmount(orderDTO.getOrderAmount().doubleValue());
        payRequest.setOrderName(ORDER_NAME);
        payRequest.setOrderId(orderDTO.getOrderId());
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        log.info("[微信支付预下单]\npayRequest={}", JsonUtil.toJson(payRequest));

        PayResponse response = bestPayService.pay(payRequest);
        log.info("[微信支付预下单]\npayResponse={}", JsonUtil.toJson(response));
    }

    @Test
    public void refund() {
        RefundResponse refundResponse = //
                payService.refund(orderService.findOne("1531971665415842679"));
        Assert.assertNotNull(refundResponse.getOutRefundNo());
    }
}