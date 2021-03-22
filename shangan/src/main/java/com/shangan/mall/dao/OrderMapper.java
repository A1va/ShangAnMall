package com.shangan.mall.dao;

import com.shangan.mall.entity.Order;
import com.shangan.util.PageQueryUtil;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository("orderMapper")
public interface OrderMapper {
    Integer getOrdersTotalNum(PageQueryUtil pageQueryUtil);
    List<Order> selectAllOrders(PageQueryUtil pageQueryUtil);
    Integer updateOrderStatus(Order order);
    Integer dayofprice(String date);
}
