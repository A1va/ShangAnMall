package com.shangan.mall.controller.Vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuIndexVo {

    @ApiModelProperty("一级导航id")
    private Integer id;

    @ApiModelProperty("一级导航名")
    private String name;

    @ApiModelProperty("二级导航列表")
    private List<MenuSecondVo> children;
}
