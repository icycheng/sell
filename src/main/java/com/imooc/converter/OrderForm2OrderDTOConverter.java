package com.imooc.converter;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.imooc.dataobject.OrderDetail;
import com.imooc.dto.OrderDTO;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.form.OrderForm;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: icych
 * @description:
 * @date: Created on 9:44 2018/7/16
 */
@Slf4j
public class OrderForm2OrderDTOConverter {

    private OrderForm2OrderDTOConverter() {
    }

    public static OrderDTO convert(OrderForm orderForm) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerOpenid(orderForm.getOpenid());
        orderDTO.setBuyerName(orderForm.getName());
        orderDTO.setBuyerPhone(orderForm.getPhone());
        orderDTO.setBuyerAddress(orderForm.getAddress());

        //items(JSON String) ==> orderDetailList
        List<OrderDetail> orderDetailList;
        Gson gson = new Gson();
        try {
            orderDetailList = gson.fromJson(orderForm.getItems(), new TypeToken<List<OrderDetail>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            log.error("[创建订单] 参数错误, string={}", orderForm.getItems());
            throw new SellException(ResultEnum.ILEGAL_ARGUMENT);
        }
        orderDTO.setOrderDetailList(orderDetailList);

        return orderDTO;
    }
}
