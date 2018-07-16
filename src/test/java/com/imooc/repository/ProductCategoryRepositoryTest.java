package com.imooc.repository;

import com.imooc.BaseTest;
import com.imooc.dataobject.ProductCategory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

/**
 * @author: icych
 * @description: ProductCategoryRepositoryTest
 * @date: Created on 20:45 2018/7/11
 */
@Slf4j
public class ProductCategoryRepositoryTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(ProductCategoryRepositoryTest.class);

    @Autowired
    private ProductCategoryRepository repository;

    @Test
    public void testFindAll() {
        List<ProductCategory> productCategoryList = repository.findAll();
        System.out.println("productCategoryList" + productCategoryList);
    }

    @Test
    @Transactional//测试通过后自动删除新增数据
    public void testSave() {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryName("测试");
        productCategory.setCategoryType(4);
        repository.save(productCategory);
    }

    @Test
    public void testUpdate() {
        ProductCategory productCategory = repository.findOne(4);
        productCategory.setCategoryName("甜点");
        productCategory.setCategoryType(3);
        repository.save(productCategory);
    }

    @Test
    public void testFindbyCategoryTypeIn() {
        List<ProductCategory> result = repository.findByCategoryTypeIn(Arrays.asList(1, 2, 3));
        logger.info("查询结果: {}", result);
        Assert.assertNotEquals(0, result.size());
    }

}