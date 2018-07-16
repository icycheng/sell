package com.imooc.repository;

import com.imooc.dataobject.OrderMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author: icych
 * @description:
 * @date: Created on 17:41 2018/7/15
 */
public interface OrderMasterRepository extends JpaRepository<OrderMaster, String> {

    /**
     * 按买家openid查询订单
     * @param buyerOpenid
     * @param pageable
     * @return
     */
    Page<OrderMaster> findByBuyerOpenid(String buyerOpenid, Pageable pageable);
}
