package com.imooc.dto;

import com.imooc.dataobject.OrderDetail;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author: icych
 * @description:
 * @date: Created on 19:16 2018/7/15
 */
@Data
public class OrderDTO {
    private String orderId;

    private String buyerName;

    private String buyerPhone;

    private String buyerAddress;

    private String buyerOpenid;

    /**
     * 订单总额
     */
    private BigDecimal orderAmount;

    /**
     * 订单状态, 默认为0新下单
     */
    private Integer orderStatus;

    /**
     * 支付状态, 默认为0等待支付
     */
    private Integer payStatus;

    private Date createTime;

    private Date updateTime;

    private List<OrderDetail> orderDetailList;
}
