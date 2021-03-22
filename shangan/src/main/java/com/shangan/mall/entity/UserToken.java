package com.shangan.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author Alva
 * @CreateTime 2021/1/25 13:24
 * 对应数据库的 Token 记录表。
 * 用户表主要是用于存储用户的基本信息等内容，
 * 而 Token 记录表则是用于存储 Token 与用户之间的关系，
 * 在用户认证时，通过 Token 字符串查询到对应的用户信息
 */
@ApiModel(value="User_token对象", description="")
@Data
public class UserToken {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户主键id")
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    @ApiModelProperty(value = "token值(32位字符串)")
    private String token;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "token过期时间")
    private Date expireTime;
}
