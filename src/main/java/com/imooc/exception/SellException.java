package com.imooc.exception;

import com.imooc.enums.ResultEnum;
import lombok.Getter;

/**
 * @author: icych
 * @description:
 * @date: Created on 19:31 2018/7/15
 */
public class SellException extends RuntimeException {

    @Getter
    private Integer code;

    public SellException(ResultEnum resultEnum) {
        super(resultEnum.getDesc());
        this.code = resultEnum.getCode();
    }

    public SellException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
