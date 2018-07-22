package com.imooc.enums;

import lombok.Getter;

/**
 * @author: icych
 * @description: 服务端返回状态码及消息
 * @date: Created on 19:29 2018/7/15
 */
@Getter
public enum ResultEnum {

    SUCCESS(0, "成功"), //

    ILEGAL_ARGUMENT(1, "参数错误"), //

    PRODUCT_NOT_EXIST(10, "商品不存在"), //

    PRODUCT_STOCK_ERROR(11, "商品库存不正确"), //

    ORDER_NOT_EXIST(12, "订单不存在"),//

    ORDER_DETAIL_NOT_EXIST(13, "订单详情不存在"), //

    ORDER_STATUS_ERROR(14, "订单状态不正确"), //

    ORDER_STATUS_UPDATE_FAILED(15, "订单状态更新失败"), //

    ORDER_DETAIL_EMPTY(16, "订单中无商品详情"),//

    PAY_STATUS_ERROR(17, "订单支付状态不正确"),

    PAY_STATUS_UPDATE_FAILED(18, "订单支付状态更新失败"),

    CART_EMPTY(19, "购物车为空"), //

    ORDER_OWNER_ERROR(20, "订单不属于当前用户"),

    WECHAT_MAP_ERROR(21, "微信公众号错误"),

    WXPAY_NOTIFY_MONEY_VERIFY_ERROR(22, "微信支付异步通知金额校验不通过"),

    ORDER_CANCEL_SUCCESS(23, "订单取消成功"),

    ORDER_FINISH_SUCCESS(24, "订单完结成功"),

    PRODUCT_STATUS_ERROR(25, "商品状态不正确"),

    LOGIN_FAILED(26, "登录失败,登录信息不正确"),

    LOGOUT_SUCCESS(27, "登出成功");

    private Integer code;
    private String desc;

    ResultEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
