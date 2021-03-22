package com.shangan.mall.entity;

import lombok.Data;

/**
 * @Author Alva
 * @CreateTime 2021/1/24 20:36
 * 库存修改所需实体
 */
@Data
public class Stock {
    private Long goodsId;

    private Integer goodsCount;
}
