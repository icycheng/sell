package com.imooc.repository;

import com.imooc.BaseTest;
import com.imooc.dataobject.ProductInfo;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: icych
 * @description:
 * @date: Created on 1:21 2018/7/15
 */
public class ProductInfoRepositoryTest extends BaseTest {

    @Autowired
    private ProductInfoRepository repository;

    @Test
    @Ignore
    public void testSave() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId("123456");
        productInfo.setProductName("皮蛋粥");
        productInfo.setProductPrice(new BigDecimal(3.2));
        productInfo.setProductStock(100);
        productInfo.setProductDescription("很好喝的粥");
        productInfo.setProductIcon("http://xxxxx.jpg");
        productInfo.setProductStatus(0);
        productInfo.setCategoryType(2);

        ProductInfo result = repository.save(productInfo);
        Assert.assertNotNull(result);
    }

    @Test
    public void findByProductStatus(String str) {
        List<ProductInfo> productInfoList = repository.findByProductStatus(0);
        Assert.assertEquals(3, productInfoList.size());
    }
}