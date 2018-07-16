package com.imooc.service;

import com.imooc.dataobject.ProductInfo;
import com.imooc.dto.CartDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author: icych
 * @description:
 * @date: Created on 1:22 2018/7/15
 */
public interface ProductService {

    ProductInfo findOne(String productId);

    /**
     * 查询所有在架商品列表
     *
     * @return
     */
    List<ProductInfo> findUpAll();

    /**
     * 分页查询所有商品
     *
     * @param pageable
     * @return
     */
    Page<ProductInfo> findAll(Pageable pageable);

    ProductInfo save(ProductInfo productInfo);

    /**
     * 减少库存
     *
     * @param cartDTOList 多件商品(商品编号+购买数量)
     */
    void decreaseStock(List<CartDTO> cartDTOList);

    /**
     * 恢复库存(取消订单)
     *
     * @param cartDTOList 多件商品(商品编号+购买数量)
     */
    void increaseStock(List<CartDTO> cartDTOList);
}
