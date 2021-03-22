package com.shangan.mall.dao;

import com.shangan.mall.controller.Vo.ParentCategoryListVo;
import com.shangan.mall.entity.Goods;
import com.shangan.mall.entity.GoodsCategory;
import com.shangan.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("goodsCategoryMapper")
public interface GoodsCategoryMapper {

    List<GoodsCategory> selectGoodsCategoryList(PageQueryUtil pageQueryUtil);

    Integer getTotalCategoryNum(PageQueryUtil pageQueryUtil);

    List<GoodsCategory> selectGoodsCategoryByParams(@Param("parentIds") List<Long> parentIds,
                                                    @Param("categoryLevel") int categoryLevel,
                                                    @Param("number") int number);

    Integer addCategory(GoodsCategory goodsCategory);

    Integer deleteCategoryById(Long categoryId);

    ParentCategoryListVo selectParentIdList(@Param("categoryId") Long categoryId,
                                            @Param("type") int type);


    Integer updateCategorySelective(GoodsCategory goodsCategory);
}
