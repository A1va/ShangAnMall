package com.shangan.mall.controller.Vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuSecondVo {

    @ApiModelProperty("二级导航id")
    private Integer id;

    @ApiModelProperty("二级导航名")
    private String authName;

    @ApiModelProperty("二级导航所指路径")
    private String path;
}
