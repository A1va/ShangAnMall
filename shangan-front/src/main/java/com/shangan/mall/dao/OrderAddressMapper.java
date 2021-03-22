package com.shangan.mall.dao;

import com.shangan.mall.entity.OrderAddress;
import org.springframework.stereotype.Repository;

/**
 * @Author Alva
 * @CreateTime 2021/2/1 17:45
 */
@Repository("orderAddressMapper")
public interface OrderAddressMapper {

    int deleteByPrimaryKey(Long orderId);

    int insert(OrderAddress record);

    int insertSelective(OrderAddress record);

    OrderAddress selectByPrimaryKey(Long orderId);

    int updateByPrimaryKeySelective(OrderAddress record);

    int updateByPrimaryKey(OrderAddress record);
}
