package com.shangan.mall.entity;

import lombok.Data;

/**
 * @Author Alva
 * @CreateTime 2021/2/1 17:39
 * 对应数据库的订单地址表
 */
@Data
public class OrderAddress {

    private Long orderId;

    private String userName;

    private String userPhone;

    private String provinceName;

    private String cityName;

    private String regionName;

    private String detailAddress;
}
