package com.shangan.mall.service;

import com.shangan.mall.controller.param.SaveCartItemParam;
import com.shangan.mall.controller.param.UpdateCartItemParam;
import com.shangan.mall.controller.vo.ShoppingCartItemVo;
import com.shangan.mall.entity.ShoppingCartItem;
import com.shangan.util.PageQueryUtil;
import com.shangan.util.PageResult;

import java.util.List;

/**
 * @Author Alva
 * @CreateTime 2021/1/30 21:17
 */
public interface ShoppingCartService {

    /**
     * 保存商品至购物车中
     * @param saveCartItemParam
     * @param userId
     * @return
     */
    String saveCartItem(SaveCartItemParam saveCartItemParam, Long userId);

    /**
     * 修改购物车中的属性
     * @param updateCartItemParam
     * @param userId
     * @return
     */
    String updateCartItem(UpdateCartItemParam updateCartItemParam, Long userId);

    /**
     * 获取购物项详情
     * @param shoppingCartItemId
     * @return
     */
    ShoppingCartItem getCartItemById(Long shoppingCartItemId);

    /**
     * 删除购物车中的商品
     * @param shoppingCartItemId
     * @return
     */
    Boolean deleteById(Long shoppingCartItemId);

    /**
     * 获取我的购物车中的列表数据
     * @param userId
     * @return
     */
    List<ShoppingCartItemVo> getMyShoppingCartItemList(Long userId);

    /**
     * 根据userId和cartItemIds获取对应的购物项记录
     * @param cartItemIds
     * @param userId
     * @return
     */
    List<ShoppingCartItemVo> getCartItemsForSettle(List<Long> cartItemIds, Long userId);

    /**
     * 我的购物车(分页数据)
     * @param pageUtil
     * @return
     */
    PageResult getMyShoppingCartItemList(PageQueryUtil pageUtil);
}
