package com.imooc.service;

import com.imooc.BaseTest;
import com.imooc.dataobject.ProductCategory;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

/**
 * @author: icych
 * @description:
 * @date: Created on 1:02 2018/7/15
 */
public class CategoryServiceTest extends BaseTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    public void findOne() {
        ProductCategory productCategory = categoryService.findOne(1);
        Assert.assertEquals("小菜", productCategory.getCategoryName());
    }

    @Test
    public void findAll() {
        List<ProductCategory> productCategoryList = categoryService.findAll();
        Assert.assertEquals(3, productCategoryList.size());
    }

    @Test
    public void findByCategoryTypeIn() {
        List<ProductCategory> productCategoryList = //
                categoryService.findByCategoryTypeIn(Arrays.asList(1, 2, 3));
        Assert.assertEquals(3, productCategoryList.size());
    }

    @Test
    @Transactional
    public void save() {
        ProductCategory category = new ProductCategory();
        category.setCategoryName("test");
        category.setCategoryType(4);
        categoryService.save(category);
    }
}