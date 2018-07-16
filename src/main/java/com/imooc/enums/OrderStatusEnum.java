package com.imooc.enums;

import lombok.Getter;

/**
 * @author: icych
 * @description: 订单状态
 * @date: Created on 17:24 2018/7/15
 */
@Getter
public enum OrderStatusEnum {
    NEW(0, "新订单"), FINISHED(1, "已完成"), CANCELED(2, "已取消");

    private Integer code;
    private String desc;

    OrderStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
