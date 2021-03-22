package com.shangan.mall.controller.Vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CategoryThirdVo {

    @ApiModelProperty("当前三级分类id")
    private Long categoryId;

    @ApiModelProperty("当前分类级别")
    private Byte categoryLevel;

    @ApiModelProperty("当前三级分类名称")
    private String categoryName;
}
