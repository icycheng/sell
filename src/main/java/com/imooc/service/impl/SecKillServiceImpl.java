package com.imooc.service.impl;

import com.imooc.exception.SellException;
import com.imooc.service.RedisLock;
import com.imooc.service.SecKillService;
import com.imooc.util.KeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: icych
 * @description: 商品秒杀
 * @date: Created on 1:17 2018/7/22
 */
@Service
public class SecKillServiceImpl implements SecKillService {

    private static final Integer LOCK_TIME_OUT = 1000 * 10;//10s

    @Autowired
    private RedisLock redisLock;

    /**
     * 国庆活动，皮蛋粥特价，限量100000份
     */
    static Map<String, Integer> products;
    static Map<String, Integer> stock;
    static Map<String, String> orders;

    static {
        /*
         * 模拟多个表，商品信息表，库存表，秒杀成功订单表
         */
        products = new HashMap<>();
        stock = new HashMap<>();
        orders = new HashMap<>();
        products.put("123456", 100000);
        stock.put("123456", 100000);
    }

    private String queryMap(String productId) {
        return "国庆活动，皮蛋粥特价，限量份" + products.get(productId) + //
                " 还剩：" + stock.get(productId) + " 份" + " 该商品成功下单用户数目：" + orders.size() + " 人";
    }

    @Override
    public String querySecKillProductInfo(String productId) {
        return this.queryMap(productId);
    }

    @Override
    public void orderProductMockDiffUser(String productId) {

        String time = String.valueOf(System.currentTimeMillis() + LOCK_TIME_OUT);

        //加锁
        boolean locked = redisLock.lock(productId, time);
        if (!locked) {
            throw new SellException(101, "哎呦喂,人太多啦,换个姿势再试试~~");
        }

        //1.查询该商品库存，为0则活动结束。
        int stockNum = stock.get(productId);
        if (stockNum == 0) {
            throw new SellException(100, "活动结束");
        } else {
            //2.下单(模拟不同用户openid不同)
            orders.put(KeyGenerator.generate(), productId);
            //3.减库存
            stockNum = stockNum - 1;
            //模拟数据库io
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stock.put(productId, stockNum);
        }
        //解锁
        redisLock.unlock(productId, time);
    }

}
