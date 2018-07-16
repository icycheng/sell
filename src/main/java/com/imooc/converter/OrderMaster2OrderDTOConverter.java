package com.imooc.converter;

import com.imooc.dataobject.OrderMaster;
import com.imooc.dto.OrderDTO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: icych
 * @description:
 * @date: Created on 21:33 2018/7/15
 */
public class OrderMaster2OrderDTOConverter {

    public static OrderDTO convert(OrderMaster orderMaster) {
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        return orderDTO;
    }

    public static List<OrderDTO> convert(List<OrderMaster> orderMasterList) {
        /*
          return orderMasterList.stream().map(e -> convert(e))
           .collect(Collectors.toList());
         */

        List<OrderDTO> orderDTOList = new ArrayList<>();
        for (OrderMaster orderMaster : orderMasterList) {
            OrderDTO orderDTO = convert(orderMaster);
            orderDTOList.add(orderDTO);
        }
        return orderDTOList;
    }
}
