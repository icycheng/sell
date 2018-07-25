package com.imooc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author: icych
 * @description: 微信相关配置
 * @date: Created on 11:07 2018/7/17
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WechatAccountConfig {

    /**
     * 公众号id
     */
    private String mpAppId;

    /**
     * 公众号密码
     */
    private String mpAppSecret;

    /**
     * 开放平台id
     */
    private String openAppId;

    /**
     * 开放平台密码
     */
    private String openAppSecret;

    /**
     * 商户号
     */
    private String mchId;

    /**
     * 商户密钥
     */
    private String mchKey;

    /**
     * 商户证书路径
     */
    private String keyPath;

    /**
     * 微信支付异步通知
     */
    private String notifyUrl;

    /**
     * 微信模板消息,有多个
     * K:消息模板id
     * V:对应的消息模板
     */
    private Map<String, String> templateId;
}
