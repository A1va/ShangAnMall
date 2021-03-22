package com.shangan.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author Alva
 * @CreateTime 2021/1/24 20:33
 * 对应数据库用户表
 */
@Data
public class User {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户主键id")
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "登陆名称(默认为手机号)")
    private String loginName;

    @ApiModelProperty(value = "MD5加密后的密码")
    private String passwordMd5;

    @ApiModelProperty(value = "个性签名")
    private String introduceSign;

    @ApiModelProperty(value = "注销标识字段(0-正常 1-已注销)")
    private Byte isDeleted;

    @ApiModelProperty(value = "锁定标识字段(0-未锁定 1-已锁定)")
    private Byte lockedFlag;

    @ApiModelProperty(value = "注册时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
