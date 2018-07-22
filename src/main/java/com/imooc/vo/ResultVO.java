package com.imooc.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: icych
 * @description: http请求返回的最外层对象
 * @date: Created on 15:31 2018/7/15
 */
@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultVO<T> implements Serializable {

    private static final long serialVersionUID = -8349181922768084604L;

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
