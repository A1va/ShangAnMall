package com.shangan.mall.controller.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author Alva
 * @CreateTime 2021/1/29 15:08
 * 共有三个分类的 VO 层对象。虽然字段类似，不过商品分类是有层级关系的，
 * 在 VO 对象中还需要定义其中的层级关系，因此又定义了分类层级字段，
 * 并且在一级分类 VO 和二级分类 VO 对象中定义了下级分类列表字段。
 * 比如二级分类中，不仅仅要包含二级分类的信息，还要包含该二级分类下所有的三级分类的信息。
 */

@Data
public class IndexCategoryVo implements Serializable {

    @ApiModelProperty("当前一级分类id")
    private Long categoryId;

    @ApiModelProperty("当前分类级别")
    private Byte categoryLevel;

    @ApiModelProperty("当前一级分类名称")
    private String categoryName;

    @ApiModelProperty("二级分类列表")
    private List<SecondLevelCategoryVo> secondLevelCategoryVoList;
}
