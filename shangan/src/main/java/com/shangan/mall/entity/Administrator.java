package com.shangan.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Alva
 * @CreateTime 2021/1/24 20:28
 * 对应数据库的管理员表
 */
@ApiModel(value="管理员对象", description="")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Administrator {

    @ApiModelProperty(value = "管理员id")
    @TableId(value = "admin_user_id", type = IdType.AUTO)
    private Long adminUserId;

    @ApiModelProperty(value = "管理员登陆名称")
    private String loginUserName;

    @ApiModelProperty(value = "管理员登陆密码")
    private String loginPassword;

    @ApiModelProperty(value = "管理员显示昵称")
    private String nickName;

    @ApiModelProperty(value = "是否锁定 0未锁定 1已锁定无法登陆")
    private Byte locked;
}
