package com.imooc.enums;

import lombok.Getter;

/**
 * @author: icych
 * @description: 商品状态
 * @date: Created on 15:02 2018/7/15
 */
@Getter
public enum ProductStatusEnum {
    UP(0, "在架"), DOWN(1, "下架");

    private Integer code;
    private String desc;

    ProductStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
