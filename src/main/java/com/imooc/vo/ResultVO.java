package com.imooc.vo;

import lombok.Data;

/**
 * @author: icych
 * @description: http请求返回的最外层对象
 * @date: Created on 15:31 2018/7/15
 */
@Data
public class ResultVO<T> {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 提示信息
     */
    private String msg;

    /**
     * 数据
     */
    private T data;
}
