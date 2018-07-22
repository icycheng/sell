package com.imooc.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author: icych
 * @description: 商品详情VO
 * @date: Created on 15:44 2018/7/15
 */
@Data
public class ProductInfoVO implements Serializable {

    private static final long serialVersionUID = 6179894408250932674L;

    @JsonProperty("id")
    private String productId;

    @JsonProperty("name")
    private String productName;

    @JsonProperty("price")
    private BigDecimal productPrice;

    @JsonProperty("description")
    private String productDescription;

    @JsonProperty("icon")
    private String productIcon;
}
