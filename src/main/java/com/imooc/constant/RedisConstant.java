package com.imooc.constant;

/**
 * @author: icych
 * @description: Redis常量
 * @date: Created on 22:52 2018/7/20
 */
public interface RedisConstant {

    /**
     * token前缀
     */
    String TOKEN_PREFIX = "token_";

    /**
     * 过期时间
     */
    Integer expire = 3600;
}
