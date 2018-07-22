package com.imooc.repository;

import com.imooc.BaseTest;
import com.imooc.dataobject.SellerInfo;
import com.imooc.util.KeyGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: icych
 * @description:
 * @date: Created on 20:42 2018/7/20
 */
@Slf4j
public class SellerInfoRepositoryTest extends BaseTest {

    @Autowired
    private SellerInfoRepository repository;

    @Test
    public void findByOpenid() {
        SellerInfo result = repository.findByOpenid("oTgZpwchb2kxo2TBxWW7obCVbm0o");
        Assert.assertNotNull(result);
    }

    @Test
    public void save() {
        SellerInfo sellerInfo = new SellerInfo();
        sellerInfo.setSellerId(KeyGenerator.generate());
        sellerInfo.setOpenid("oTgZpwchb2kxo2TBxWW7obCVbm0o");
        sellerInfo.setUsername("admin");
        sellerInfo.setPassword("admin");
        SellerInfo result = repository.save(sellerInfo);
        Assert.assertNotNull(result);
    }
}