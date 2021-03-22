package com.shangan.mall.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Rights {

    @ApiModelProperty("管理员Id")
    private Long adminUserId;

    @ApiModelProperty("用户管理权限标识位")
    private boolean userManagement;

    @ApiModelProperty("订单管理权限标识位")
    private boolean orderManagement;

    @ApiModelProperty("商品管理权限标识位")
    private boolean goodsManagement;

    @ApiModelProperty("日志管理权限标识位")
    private boolean logManagement;

    @ApiModelProperty("超级管理员标识位")
    private boolean isSuper;

}
