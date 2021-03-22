package com.shangan.mall.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Author Alva
 * @CreateTime 2021/2/1 17:37
 * 对应数据库的用户地址表
 */
@Data
public class UserAddress {

    private Long addressId;

    private Long userId;

    private String userName;

    private String userPhone;

    private Byte defaultFlag;

    private String provinceName;

    private String cityName;

    private String regionName;

    private String detailAddress;

    private Byte isDeleted;

    private Date createTime;

    private Date updateTime;
}
