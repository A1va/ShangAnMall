package com.shangan.mall.dao;

import com.shangan.mall.entity.ShoppingCartItem;
import com.shangan.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Alva
 * @CreateTime 2021/1/30 21:14
 */
@Repository("shoppingCartItemMapper")
public interface ShoppingCartItemMapper {

    int deleteByPrimaryKey(Long cartItemId);

    int insert(ShoppingCartItem record);

    int insertSelective(ShoppingCartItem record);

    ShoppingCartItem selectByPrimaryKey(Long cartItemId);

    ShoppingCartItem selectByUserIdAndGoodsId(@Param("userId") Long userId, @Param("goodsId") Long goodsId);

    List<ShoppingCartItem> selectByUserId(@Param("userId") Long userId, @Param("number") int number);

    List<ShoppingCartItem> selectByUserIdAndCartItemIds(@Param("userId") Long userId, @Param("cartItemIds") List<Long> cartItemIds);

    int selectCountByUserId(Long newBeeMallUserId);

    int updateByPrimaryKeySelective(ShoppingCartItem record);

    int updateByPrimaryKey(ShoppingCartItem record);

    int deleteBatch(List<Long> ids);

    List<ShoppingCartItem> findMyCartItems(PageQueryUtil pageUtil);

    int getTotalMyCartItems(PageQueryUtil pageUtil);
}
