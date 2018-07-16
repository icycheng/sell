package com.imooc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: icych
 * @description: 购物车
 * @date: Created on 20:02 2018/7/15
 */
@Data
@AllArgsConstructor
public class CartDTO {

    private String productId;

    private Integer productQuantity;

}
