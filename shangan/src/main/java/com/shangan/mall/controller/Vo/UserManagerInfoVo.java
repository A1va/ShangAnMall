package com.shangan.mall.controller.Vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class UserManagerInfoVo {
    @ApiModelProperty(value = "用户主键id")
    private Long userId;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "登陆名称(默认为手机号)")
    private String loginName;

    @ApiModelProperty(value = "个性签名")
    private String introduceSign;

    @ApiModelProperty(value = "锁定标识字段(0-未锁定 1-已锁定)")
    private Boolean status;

    @ApiModelProperty(value = "锁定标识")
    private Byte lockedFlag;

    @ApiModelProperty(value = "注册时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
