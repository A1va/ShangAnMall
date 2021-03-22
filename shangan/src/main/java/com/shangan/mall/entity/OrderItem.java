package com.shangan.mall.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Author Alva
 * @CreateTime 2021/1/24 20:35
 * 订单项
 */
@Data
public class OrderItem {
    private Long orderItemId;

    private Long orderId;

    private Long goodsId;

    private String goodsName;

    private String goodsCoverImg;

    private Integer sellingPrice;

    private Integer goodsCount;

    private Date createTime;
}
