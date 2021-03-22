package com.shangan.mall.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class GoodsAddParam {

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("商品参数")
    private String goodsIntro;

    @ApiModelProperty("商品所属分类Id")
    private Long goodsCategoryId;

    @ApiModelProperty("商品封面图路径")
    private String goodsCoverImg;

    @ApiModelProperty("商品标签")
    private String tag;

    @ApiModelProperty("商品成本价")
    private Integer originalPrice;

    @ApiModelProperty("商品利润")
    private Integer profit;

    @ApiModelProperty("商品库存")
    private Integer stockNum;

    @ApiModelProperty("商品详细说明")
    private String goodsDetailContent;
}
