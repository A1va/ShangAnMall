package com.shangan.mall.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Author Alva
 * @CreateTime 2021/1/26 17:50
 * 对应数据库用户的 token 表
 */
@Data
public class UserToken {
    private Long userId;

    private String token;

    private Date updateTime;

    private Date expireTime;
}
