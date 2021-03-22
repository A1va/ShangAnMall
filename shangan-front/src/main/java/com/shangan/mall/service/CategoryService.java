package com.shangan.mall.service;

import com.shangan.mall.controller.vo.IndexCategoryVo;
import com.shangan.mall.entity.GoodsCategory;

import java.util.List;

/**
 * @Author Alva
 * @CreateTime 2021/1/29 15:15
 */
public interface CategoryService {

    String saveCategory(GoodsCategory goodsCategory);

    String updateGoodsCategory(GoodsCategory goodsCategory);

    GoodsCategory getGoodsCategoryById(Long id);

    Boolean deleteBatch(Integer[] ids);

    /**
     * 返回分类数据(首页调用)
     * @return
     */
    List<IndexCategoryVo> getCategoriesForIndex();
}
