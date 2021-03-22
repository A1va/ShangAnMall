package com.shangan.mall.service;

import com.shangan.mall.controller.Vo.CategoryFirstVo;
import com.shangan.mall.controller.param.CategoryEditParam;
import com.shangan.mall.entity.Administrator;
import com.shangan.mall.entity.GoodsCategory;
import com.shangan.util.PageQueryUtil;
import com.shangan.util.PageResult;

import java.util.List;

public interface GoodsCategoryService {

    PageResult getGoodsCategoryList(PageQueryUtil pageQueryUtil);

    List<CategoryFirstVo> getAllCategoryList();

    List<CategoryFirstVo> getParentCategoryList();

    Boolean addCategory(GoodsCategory goodsCategory);

    Boolean deleteCategory(Long categoryId);

    List<Long> getParentCategoryListById(Long categoryId, int type);

    Boolean editCategory(CategoryEditParam goodsEditParam, Administrator administrator);

}
