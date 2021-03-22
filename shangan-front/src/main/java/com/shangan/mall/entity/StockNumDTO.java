package com.shangan.mall.entity;

import lombok.Data;

/**
 * @Author Alva
 * @CreateTime 2021/2/1 17:42
 * 库存修改所需实体
 */
@Data
public class StockNumDTO {

    private Long goodsId;

    private Integer goodsCount;
}
