package com.imooc.aspect;

import com.imooc.constant.CookieConstant;
import com.imooc.constant.RedisConstant;
import com.imooc.exception.SellerException;
import com.imooc.util.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author: icych
 * @description: 卖家统一身份认证
 * @date: Created on 11:12 2018/7/21
 */
@Aspect
@Component
@Slf4j
public class SellerAuthorizeAspect {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 切入的方法,排除SellerUserController
     */
    @Pointcut("execution(public * com.imooc.controller.Seller*.*(..))" //
            + "&& !execution(public * com.imooc.controller.SellerUserController.*(..))")
    public void veriryPointCut() {
    }

    @Before("veriryPointCut()")
    public void verify() {
        //拿到httprequest
        ServletRequestAttributes requestAttributes = //
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        //检查cookie中是否存在token
        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
        if (cookie == null) {
            log.warn("[卖家登录认证] cookie中查询不到token");
            throw new SellerException();
        }
        //检查redis中是否存在token
        String tokenValue = redisTemplate.opsForValue() //
                .get(RedisConstant.TOKEN_PREFIX.concat(cookie.getValue()));
        if (StringUtils.isBlank(tokenValue)) {
            log.warn("[卖家登录认证] redis中查询不到tokenValue");
            throw new SellerException();
        }

    }
}
