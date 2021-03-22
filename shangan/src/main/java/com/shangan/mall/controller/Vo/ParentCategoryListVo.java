package com.shangan.mall.controller.Vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ParentCategoryListVo {

    @ApiModelProperty("一级分类ID")
    private Long firstId;

    @ApiModelProperty("二级分类ID")
    private Long secondId;

    @ApiModelProperty("三级分类ID")
    private Long thirdId;
}
