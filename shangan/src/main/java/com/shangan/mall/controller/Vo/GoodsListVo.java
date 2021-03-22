package com.shangan.mall.controller.Vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class GoodsListVo {

    @ApiModelProperty("商品编号")
    private Long goodsId;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("商品参数")
    private String goodsIntro;

    @ApiModelProperty("商品成本价")
    private Integer originalPrice;

    @ApiModelProperty("商品售价")
    private Integer sellingPrice;

    @ApiModelProperty("库存")
    private Integer stockNum;

    @ApiModelProperty("商品上架状态")
    private Byte goodsSellStatu;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
