package com.shangan.mall.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author Alva
 * @CreateTime 2021/1/30 21:19
 */
@Data
public class UpdateCartItemParam {

    @ApiModelProperty("购物项id")
    private Long cartItemId;

    @ApiModelProperty("商品数量")
    private Integer goodsCount;

    @ApiModelProperty("操作")
    private String operation;

    @ApiModelProperty("选中标志")
    private Integer checked;
}
