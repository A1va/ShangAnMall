package com.shangan.mall.dao;

import com.shangan.mall.entity.OrderItem;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Alva
 * @CreateTime 2021/2/2 15:44
 */
@Repository("orderItemMapper")
public interface OrderItemMapper {

    /**
     * 根据订单的 Id 删除订单项
     * @param orderItemId
     * @return
     */
    int deleteByPrimaryKey(Long orderItemId);

    /**
     * 新建订单项
     * @param record
     * @return
     */
    int insert(OrderItem record);

    /**
     * 批量删除已选的订单项
     * @param record
     * @return
     */
    int insertSelective(OrderItem record);

    /**
     * 根据订单项 Id 获取订单项
     * @param orderItemId
     * @return
     */
    OrderItem selectByPrimaryKey(Long orderItemId);

    /**
     * 根据订单 id 获取订单项列表
     * @param orderId
     * @return
     */
    List<OrderItem> selectByOrderId(Long orderId);

    /**
     * 根据订单列表 ids 获取订单项列表
     * @param orderIds
     * @return
     */
    List<OrderItem> selectByOrderIds(@Param("orderIds") List<Long> orderIds);

    /**
     * 批量新建订单项数据
     * @param orderItems
     * @return
     */
    int insertBatch(@Param("orderItems") List<OrderItem> orderItems);

    /**
     * 批量更新订单项的数据
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(OrderItem record);

    /**
     * 更新订单项数据
     * @param record
     * @return
     */
    int updateByPrimaryKey(OrderItem record);
}
