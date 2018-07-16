package com.imooc.util;

import java.util.Random;

/**
 * @author: icych
 * @description: 生成主键
 * 格式: 时间+随机数
 * @date: Created on 19:46 2018/7/15
 */
public class KeyGenerator {

    private KeyGenerator() {
    }

    public static synchronized String generate() {
        Random random = new Random();
        Integer number = random.nextInt(900000) + 100000;
        return System.currentTimeMillis() + String.valueOf(number);
    }
}
