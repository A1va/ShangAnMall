package com.shangan.mall.controller.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author Alva
 * @CreateTime 2021/1/27 16:46
 * VO层 用于从后端数据库返回前端的数据
 * 为了保护数据库的数据隐秘性，只返回需要的属性
 */
@Data
public class UserVo implements Serializable {

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("用户登录名")
    private String loginName;

    @ApiModelProperty("个性签名")
    private String introduceSign;
}
