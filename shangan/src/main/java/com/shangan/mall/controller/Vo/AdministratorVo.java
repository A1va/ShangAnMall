package com.shangan.mall.controller.Vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class AdministratorVo implements Serializable {

    @ApiModelProperty("管理员登录名")
    private String loginUserName;

    @ApiModelProperty("管理员昵称")
    private String nickName;
}
