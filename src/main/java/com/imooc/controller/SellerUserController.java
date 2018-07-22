package com.imooc.controller;

import com.imooc.config.ProjectUrlConfig;
import com.imooc.constant.CookieConstant;
import com.imooc.constant.RedisConstant;
import com.imooc.dataobject.SellerInfo;
import com.imooc.enums.ResultEnum;
import com.imooc.service.SellerService;
import com.imooc.util.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author: icych
 * @description: 卖家用户
 * @date: Created on 22:27 2018/7/20
 */
@Controller
@RequestMapping("/seller")
@Slf4j
public class SellerUserController {

    @Autowired
    private SellerService sellerService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ProjectUrlConfig config;

    @GetMapping("/login")
    public ModelAndView login(@RequestParam("openid") String openid, //
                              Map<String, Object> map, //
                              HttpServletResponse response) {
        //1.openid与数据库里的数据匹配
        SellerInfo sellerInfo = sellerService.findSellerInfoByOpenid(openid);
        if (sellerInfo == null) {
            map.put("msg", ResultEnum.LOGIN_FAILED.getDesc());
            map.put("returnUrl", "seller/index");
            return new ModelAndView("common/error", map);
        }

        //2.将openid写入redis, key为token_UUID串,value为openid
        String token = UUID.randomUUID().toString();
        Integer expire = RedisConstant.expire;

        redisTemplate.opsForValue().set(RedisConstant.TOKEN_PREFIX + token, //
                openid, expire, TimeUnit.SECONDS);
        log.info("Redis写入token, key={}, value={}, expire={}", //
                RedisConstant.TOKEN_PREFIX + token, openid, expire);

        //3.将token写入cookie, key为token, value为UUID串
        CookieUtil.set(response, CookieConstant.TOKEN, token, CookieConstant.expire);

        return new ModelAndView("redirect:" + config.getSell() + "/sell/seller/order/list");
    }

    @GetMapping("/logout")
    public ModelAndView logout(HttpServletRequest request, //
                               Map<String, Object> map, //
                               HttpServletResponse response) {

        //1.从cookie中查询
        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
        if (cookie != null) {
            //2.清除redis
            redisTemplate.opsForValue().getOperations() //
                    .delete(RedisConstant.TOKEN_PREFIX + cookie.getValue());
            //3.清除cookie
            CookieUtil.set(response, CookieConstant.TOKEN, null, 0);
        }
//        map.put("msg", ResultEnum.LOGOUT_SUCCESS.getDesc());
//        map.put("returnUrl", "/seller/index");
        return new ModelAndView("redirect:" + config.getBackendAuthorizeUrl());
    }

    @GetMapping("/index")
    public ModelAndView welcome() {
        return new ModelAndView("index");
    }

}
