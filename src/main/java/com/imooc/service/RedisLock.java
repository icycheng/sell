package com.imooc.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author: icych
 * @description: Redis分布式锁
 * @date: Created on 16:54 2018/7/22
 */
@Component
@Slf4j
public class RedisLock {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 加锁, 在redis里设置一个key-value,多个线程操作它互斥
     * redis是单线程的,对一个操作只能有一个线程去执行
     *
     * @param key   加锁对象的key
     * @param value 当前时间 + 超时时间
     * @return 是否获得锁
     */
    public boolean lock(String key, String value) {
        //当前key不存在, 可以成功设置k-v,返回true
        if (redisTemplate.opsForValue().setIfAbsent(key, value)) {
            return true;
        }

        //防止死锁(上一次加锁后未解锁的情况),并且保证只有一个线程能拿到锁
        String currentVal = redisTemplate.opsForValue().get(key);
        //如果锁已经过期
        if (StringUtils.isNotBlank(currentVal) //
                && Long.parseLong(currentVal) < System.currentTimeMillis()) {
            //获取上一个锁的时间戳
            String oldVal = redisTemplate.opsForValue().getAndSet(key, value);
            return StringUtils.isNotBlank(oldVal) && oldVal.equals(currentVal);
        }
        return false;
    }

    /**
     * 解锁
     *
     * @param key   加锁对象的key
     * @param value 加锁时设置的value(这里为时间戳)
     */
    public void unlock(String key, String value) {
        try {
            String currentVal = redisTemplate.opsForValue().get(key);
            if (StringUtils.isNotBlank(currentVal) && currentVal.equals(value)) {
                redisTemplate.opsForValue().getOperations().delete(key);
            }
        } catch (Exception e) {
            log.error("[redis分布式锁] 解锁异常, {}", e);
        }
    }

}
