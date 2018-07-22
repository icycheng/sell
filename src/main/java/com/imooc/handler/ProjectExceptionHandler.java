package com.imooc.handler;

import com.imooc.config.ProjectUrlConfig;
import com.imooc.exception.SellException;
import com.imooc.exception.SellerException;
import com.imooc.util.ResultVOUtil;
import com.imooc.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author: icych
 * @description: 统一处理异常
 * @date: Created on 11:45 2018/7/21
 */
@ControllerAdvice
public class ProjectExceptionHandler {

    @Autowired
    private ProjectUrlConfig config;

    //拦截登录异常
    @ExceptionHandler(value = {SellerException.class})
    public ModelAndView handleAuthorizeException() {
        return new ModelAndView("redirect:".concat(config.getBackendAuthorizeUrl()));
    }

    //拦截业务异常
    @ExceptionHandler(value = {SellException.class})
    //可以设置http返回状态码
    @ResponseBody
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public ResultVO handleSellException(SellException e) {
        return ResultVOUtil.error(e.getCode(), e.getMessage());
    }
}
