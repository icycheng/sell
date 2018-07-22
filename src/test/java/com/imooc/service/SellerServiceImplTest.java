package com.imooc.service;

import com.imooc.BaseTest;
import com.imooc.dataobject.SellerInfo;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: icych
 * @description:
 * @date: Created on 20:54 2018/7/20
 */
public class SellerServiceImplTest extends BaseTest {

    @Autowired
    private SellerService service;

    @Test
    public void findSellerInfoByOpenid() {
        SellerInfo result = service.findSellerInfoByOpenid("oTgZpwchb2kxo2TBxWW7obCVbm0o");
        Assert.assertNotNull(result);
    }
}