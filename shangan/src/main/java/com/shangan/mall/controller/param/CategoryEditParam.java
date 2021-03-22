package com.shangan.mall.controller.param;

import lombok.Data;

@Data
public class CategoryEditParam {

    private Long categoryId;

    private Byte categoryLevel;

    private Long parentId;

    private String categoryName;
}
