package com.imooc.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: icych
 * @description: 商品(包含类目)
 * @date: Created on 15:40 2018/7/15
 */
@Data
public class ProductVO implements Serializable {

    private static final long serialVersionUID = -3555231358992190972L;

    @JsonProperty("name")
    private String categoryName;

    @JsonProperty("type")
    private Integer categoryType;

    @JsonProperty("foods")
    private List<ProductInfoVO> productInfoVOList;

}
