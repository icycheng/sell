package com.imooc.service;

import com.imooc.dataobject.SellerInfo;

/**
 * @author: icych
 * @description: 卖家端
 * @date: Created on 20:52 2018/7/20
 */
public interface SellerService {

    SellerInfo findSellerInfoByOpenid(String openid);
}
