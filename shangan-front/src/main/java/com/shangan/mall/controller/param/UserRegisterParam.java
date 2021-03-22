package com.shangan.mall.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @Author Alva
 * @CreateTime 2021/1/26 21:30
 * 接收前端注册操作的参数类
 */
@Data
public class UserRegisterParam {

    @ApiModelProperty("登录名")
    @NotEmpty(message = "登录名不能为空")
    private String loginName;

    @ApiModelProperty("用户密码")
    @NotEmpty(message = "密码不能为空")
    private String password;
}
