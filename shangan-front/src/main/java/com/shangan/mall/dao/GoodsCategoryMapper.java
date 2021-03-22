package com.shangan.mall.dao;

import com.shangan.mall.entity.GoodsCategory;
import com.shangan.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Alva
 * @CreateTime 2021/1/29 15:21
 */
@Repository("goodsCategoryMapper")
public interface GoodsCategoryMapper {

    int deleteByPrimaryKey(Long categoryId);

    int insert(GoodsCategory record);

    int insertSelective(GoodsCategory record);

    GoodsCategory selectByPrimaryKey(Long categoryId);

    GoodsCategory selectByLevelAndName(@Param("categoryLevel") Byte categoryLevel,
                                       @Param("categoryName") String categoryName);

    int updateByPrimaryKeySelective(GoodsCategory record);

    int updateByPrimaryKey(GoodsCategory record);

    List<GoodsCategory> findGoodsCategoryList(PageQueryUtil pageUtil);

    int getTotalGoodsCategories(PageQueryUtil pageUtil);

    int deleteBatch(Integer[] ids);

    /**
     * 方法中，定义了三个字段，分别是 parentIds、categoryLevel、number，即分类的父类 id 集合、分类级别和需要返回的查询数量。
     * SQL 为 SELECT 语句，根据传入的参数查询固定数量的记录，如果传参 number 大于 0 我们就对数量进行过滤，
     * 否则会查询并返回所有条数的数据。
     * @param parentIds
     * @param categoryLevel
     * @param number
     * @return
     */
    List<GoodsCategory> selectByLevelAndParentIdsAndNumber(@Param("parentIds") List<Long> parentIds,
                                                           @Param("categoryLevel") int categoryLevel,
                                                           @Param("number") int number);
}
