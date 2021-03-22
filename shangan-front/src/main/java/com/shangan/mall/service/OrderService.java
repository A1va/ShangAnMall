package com.shangan.mall.service;

import com.shangan.mall.controller.vo.OrderDetailVo;
import com.shangan.mall.controller.vo.ShoppingCartItemVo;
import com.shangan.mall.entity.User;
import com.shangan.mall.entity.UserAddress;
import com.shangan.util.PageQueryUtil;
import com.shangan.util.PageResult;

import java.util.List;

/**
 * @Author Alva
 * @CreateTime 2021/2/2 15:34
 */
public interface OrderService {
    /**
     * 获取订单详情
     * @param orderNo
     * @param userId
     * @return
     */
    OrderDetailVo getOrderDetailByOrderNo(String orderNo, Long userId);

    /**
     * 我的订单列表
     * @param pageUtil
     * @return
     */
    PageResult getMyOrders(PageQueryUtil pageUtil);

    /**
     * 手动取消订单
     * @param orderNo
     * @param userId
     * @return
     */
    String cancelOrder(String orderNo, Long userId);

    /**
     * 确认收货
     * @param orderNo
     * @param userId
     * @return
     */
    String finishOrder(String orderNo, Long userId);

    String paySuccess(String orderNo, int payType);

    String saveOrder(User loginUser, UserAddress address, List<ShoppingCartItemVo> itemsForSave);

}