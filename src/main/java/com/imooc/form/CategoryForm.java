package com.imooc.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @author: icych
 * @description: 分类对象表单
 * @date: Created on 19:39 2018/7/20
 */
@Data
public class CategoryForm {

    private Integer categoryId;

    @NotBlank(message = "类目名称必填")
    private String categoryName;

    @NotNull(message = "类目类型必填")
    private Integer categoryType;
}
