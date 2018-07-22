package com.imooc.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author: icych
 * @description: 产品表单验证
 * @date: Created on 0:47 2018/7/16
 */
@Data
public class ProductForm {

    private String productId;

    /**
     * 产品名称
     */
    @NotBlank(message = "产品名称必填")
    private String productName;

    /**
     * 产品价格
     */
    @NotNull(message = "产品价格必填")
    @Min(value = 0, message = "价格不能小于0")
    private BigDecimal productPrice;

    /**
     * 产品库存
     */
    @NotNull(message = "库存必填")
    @Min(value = 0, message = "库存不能小于0")
    private Integer productStock;

    private String productDescription;

    private String productIcon;

//    private Integer productStatus;

    @NotNull(message = "商品类别必填")
    private Integer categoryType;

}
