package com.shangan.mall.controller.Vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GoodsCategoryListVo {

    @ApiModelProperty("分类id")
    private Long categoryId;

    @ApiModelProperty("分类等级")
    private Byte categoryLevel;

    @ApiModelProperty("分类名称")
    private String categoryName;

    @ApiModelProperty("有效位")
    private Byte isDeleted;

}
