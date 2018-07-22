package com.imooc.util;

import com.imooc.enums.CodeEnum;

/**
 * @author: icych
 * @description: 根据传入code值获取对应enum
 * @date: Created on 0:15 2018/7/20
 */
public class EnumUtil {

    private EnumUtil() {

    }

    public static <T extends CodeEnum> T getByCode(Integer code, Class<T> enumClass) {
        for (T enumObj : enumClass.getEnumConstants()) {
            if (code.equals(enumObj.getCode())) {
                return enumObj;
            }
        }
        return null;
    }
}
