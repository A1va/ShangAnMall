package com.shangan.mall.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author Alva
 * @CreateTime 2021/1/26 20:07
 * 接收前端修改用户信息操作的参数类
 */
@Data
public class UserUpdateParam {
    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("用户密码(需要MD5加密)")
    private String passwordMd5;

    @ApiModelProperty("个性签名")
    private String introduceSign;
}
