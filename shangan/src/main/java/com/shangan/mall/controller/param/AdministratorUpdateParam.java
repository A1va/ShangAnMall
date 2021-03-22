package com.shangan.mall.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author Alva
 * @CreateTime 2021/1/25 15:05
 * 用户修改 param
 */
@Data
public class AdministratorUpdateParam {

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("用户密码(需要MD5加密)")
    private String passwordMd5;

}
