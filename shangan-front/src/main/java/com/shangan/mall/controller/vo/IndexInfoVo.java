package com.shangan.mall.controller.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author Alva
 * @CreateTime 2021/1/28 13:16
 * 轮播图可以返回多张，需要返回 List 类型，推荐商品有三种类型，
 * 每种类型也是多条商品数据，也是 List 类型的数据
 */
@Data
public class IndexInfoVo implements Serializable {

    @ApiModelProperty("轮播图(列表)")
    private List<IndexCarouselVo> carousels;

    @ApiModelProperty("首页热销商品(列表)")
    private List<IndexConfigGoodsVo> hotGoodses;

    @ApiModelProperty("首页新品推荐(列表)")
    private List<IndexConfigGoodsVo> newGoodses;

    @ApiModelProperty("首页推荐商品(列表)")
    private List<IndexConfigGoodsVo> recommendGoodses;
}
