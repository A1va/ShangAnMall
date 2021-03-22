package com.shangan.mall.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Author Alva
 * @CreateTime 2021/1/30 21:06
 * 对应数据库中购物车单项商品的实体类
 */
@Data
public class ShoppingCartItem {

    private Long cartItemId;

    private Long userId;

    private Long goodsId;

    private Integer goodsCount;

    private Byte isDeleted;

    private Byte checked;

    private Date createTime;

    private Date updateTime;

}
