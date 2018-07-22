package com.imooc.enums;

import lombok.Getter;

/**
 * @author: icych
 * @description: 商品状态
 * @date: Created on 15:02 2018/7/15
 */
@Getter
public enum PayStatusEnum implements CodeEnum{
    WAIT(0, "等待支付"), SUCCESS(1, "支付成功"), REFUNDED(2, "已退款");

    private Integer code;
    private String desc;

    PayStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
