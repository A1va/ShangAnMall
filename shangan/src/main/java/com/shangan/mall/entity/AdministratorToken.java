package com.shangan.mall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author Alva
 * @CreateTime 2021/1/25 17:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdministratorToken {
    private Long administratorId;

    private String token;

    private Date updateTime;

    private Date expireTime;
}
