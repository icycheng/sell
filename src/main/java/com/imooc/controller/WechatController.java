package com.imooc.controller;

import com.imooc.config.ProjectUrlConfig;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;

/**
 * @author: icych
 * @description: 微信公众号登录授权与拉取用户openid
 * @date: Created on 10:53 2018/7/17
 */
@Controller
@RequestMapping("/wechat")
@Slf4j
public class WechatController {

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private WxMpService wxOpenService;

    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    @SuppressWarnings("deprecation")
    @GetMapping("/authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl) {

        //1.配置 WechatAccountConfig

        //2.调用方法
        //String url = "http://icyc.natapp1.cc/sell/wechat/userInfo";
        String url = projectUrlConfig.getWechatMpAuthorize();

        //构建redirectUrl:微信授权链接
        String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(URLEncoder.encode(url), //
                WxConsts.OAuth2Scope.SNSAPI_USERINFO, URLEncoder.encode(returnUrl));

        return "redirect:" + redirectUrl;
        //如果用户同意授权，页面将跳转至 redirect_uri/?code=CODE&state=STATE
        //在这里CODE为同意授权后获取的code, STATE为获取用户信息成功之后跳转的returnUrl
    }

    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("code") String code, //
                           @RequestParam("state") String returnUrl) {

        WxMpOAuth2AccessToken wxMpOAuth2AccessToken;
        try {
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
        } catch (WxErrorException e) {
            log.error("[微信网页授权] 异常:{}", e);
            throw new SellException(ResultEnum.WECHAT_MAP_ERROR.getCode(),//
                    e.getError().getErrorMsg());
        }

        String openid = wxMpOAuth2AccessToken.getOpenId();

        WxMpUser wxMpUser;
        try {
            wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, "zh_CN");
        } catch (WxErrorException e) {
            log.error("[微信网页授权] 拉取用户信息异常:{}", e);
            throw new SellException(ResultEnum.WECHAT_MAP_ERROR.getCode(),//
                    e.getError().getErrorMsg());
        }

        log.info("获取到用户信息:\n wxMpUser={}", wxMpUser);

        return "redirect:" + returnUrl + "?openid=" + openid;
    }

    @SuppressWarnings("deprecation")
    @GetMapping("/qrAuthorize")
    public String qrAuthorize(@RequestParam("returnUrl") String returnUrl) {

        //1.配置 WechatAccountConfig

        //2.调用方法
//        String url = "http://icyc.natapp1.cc/sell/wechat/qrUserInfo";
        String url = projectUrlConfig.getWechatOpenAuthorize();

        //构建redirectUrl:微信授权链接
        String redirectUrl = wxOpenService.buildQrConnectUrl(URLEncoder.encode(url), //
                WxConsts.QrConnectScope.SNSAPI_LOGIN, URLEncoder.encode(returnUrl));

        return "redirect:" + redirectUrl;
        //如果用户同意授权，页面将跳转至
        //https://open.wexin.qq.comxxx各种参数xxxredirect_uri/?code=CODE&state=STATE
        //在这里CODE为同意授权后获取的code, STATE为获取用户信息成功之后跳转的returnUrl
    }

    @GetMapping("/qrUserInfo")
    public String qrUserInfo(@RequestParam("code") String code, //
                             @RequestParam(value = "state", required = false) String returnUrl) {

        //用上一步的code换取accesstoken
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken;
        try {
            wxMpOAuth2AccessToken = wxOpenService.oauth2getAccessToken(code);
        } catch (WxErrorException e) {
            log.error("[微信登录授权] 出现异常:{}", e);
            throw new SellException(ResultEnum.WECHAT_MAP_ERROR.getCode(),//
                    e.getError().getErrorMsg());
        }

        String openid = wxMpOAuth2AccessToken.getOpenId();

        WxMpUser wxMpUser;
        try {
            wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, "zh_CN");
        } catch (WxErrorException e) {
            log.error("[微信登录授权] 拉取用户信息异常:{}", e);
            throw new SellException(ResultEnum.WECHAT_MAP_ERROR.getCode(),//
                    e.getError().getErrorMsg());
        }

        log.info("获取到用户信息:\n wxMpUser={}", wxMpUser);

        return "redirect:" + projectUrlConfig.getSell() + "/sell/seller/login?openid=" + openid;
    }

}
