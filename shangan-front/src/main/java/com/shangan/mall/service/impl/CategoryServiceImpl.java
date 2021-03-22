package com.shangan.mall.service.impl;

import com.shangan.common.CategoryLevelEnum;
import com.shangan.common.Constants;
import com.shangan.common.ServiceResultEnum;
import com.shangan.mall.controller.vo.IndexCategoryVo;
import com.shangan.mall.controller.vo.SecondLevelCategoryVo;
import com.shangan.mall.controller.vo.ThirdLevelCategoryVo;
import com.shangan.mall.dao.GoodsCategoryMapper;
import com.shangan.mall.entity.GoodsCategory;
import com.shangan.mall.service.CategoryService;
import com.shangan.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * @Author Alva
 * @CreateTime 2021/1/29 15:15
 */
@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {

    private final GoodsCategoryMapper goodsCategoryMapper;

    public CategoryServiceImpl(GoodsCategoryMapper goodsCategoryMapper) {
        this.goodsCategoryMapper = goodsCategoryMapper;
    }

    @Override
    public String saveCategory(GoodsCategory goodsCategory) {
//        查找数据库是否已存在对应的分类信息
        GoodsCategory temp = goodsCategoryMapper.
                selectByLevelAndName(goodsCategory.getCategoryLevel(),
                        goodsCategory.getCategoryName());
//        是则返回相同分类已存在的 Result 结果
        if (temp != null) {
            return ServiceResultEnum.SAME_CATEGORY_EXIST.getResult();
        }

//        调用 insertSelective 插入分类信息，并判断是否出错
        if (goodsCategoryMapper.insertSelective(goodsCategory) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateGoodsCategory(GoodsCategory goodsCategory) {

        GoodsCategory temp = goodsCategoryMapper.selectByPrimaryKey(goodsCategory.getCategoryId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        GoodsCategory temp2 = goodsCategoryMapper.selectByLevelAndName(goodsCategory.getCategoryLevel(), goodsCategory.getCategoryName());
        if (temp2 != null && !temp2.getCategoryId().equals(goodsCategory.getCategoryId())) {
//            同名且不同 id 不能继续修改
            return ServiceResultEnum.SAME_CATEGORY_EXIST.getResult();
        }
        goodsCategory.setUpdateTime(new Date());
        if (goodsCategoryMapper.updateByPrimaryKeySelective(goodsCategory) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public GoodsCategory getGoodsCategoryById(Long id) {
        return goodsCategoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public Boolean deleteBatch(Integer[] ids) {
        if (ids.length < 1) {
            return false;
        }
//        删除分类数据
        return goodsCategoryMapper.deleteBatch(ids) > 0;
    }

    /**
     * 首先读取固定数量的一级分类数据，之后获取二级分类数据并设置到对应的一级分类下，
     * 最后是获取和设置每一个二级分类下的三级分类数据，这里的逻辑是一次性读取所有数据并返回，
     * 将所有三级分类数据读取并渲染到页面上，
     * 所以这里就将所有的数据都读取出来并封装成一个对象返回给视图层。
     * @return IndexCategoryVo List
     */
    @Override
    public List<IndexCategoryVo> getCategoriesForIndex() {

        List<IndexCategoryVo> indexCategoryVoList = new ArrayList<>();

        /*
          获取一级分类中固定数量的数据。
          因为一级分类是没有父节点的，即父级分类的 id 为缺省值 0，同时 parentIds 参数为 List 类型，
          所以这里 parentIds 参数我们传的就是 Collections.singletonList(0L)；
          分类级别传的就是 1，这里用的是枚举类 CategoryLevelEnum.LEVEL_ONE；
          分类数量这里用的也是一个常量 Constants.INDEX_CATEGORY_NUMBER，默认为 10。
         */
        List<GoodsCategory> firstLevelCategories = goodsCategoryMapper.
                selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L),
                        CategoryLevelEnum.LEVEL_ONE.getLevel(), Constants.INDEX_CATEGORY_NUMBER);

//        判断一级分类的数据是否为非空，是则继续
        if (!CollectionUtils.isEmpty(firstLevelCategories)) {
//            获取一级分类数据中所有分类的 ID，封装成 List
            List<Long> firstLevelCategoryIds = firstLevelCategories.stream().
                    map(GoodsCategory::getCategoryId).collect(Collectors.toList());

            /*
              通过获取到的一级分类数据中的 ID，获取对应 ID 中二级分类的数据。
              因为上一步已经获取到所有的一级分类列表数据，所以我们把其中的 id 字段全部获取出来并放入到一个 List 对象中，
              这里 parentIds 参数我们传的就是 firstLevelCategoryIds；
              分类级别传的是 2，用的是枚举类 CategoryLevelEnum.LEVEL_TWO；
              数量 number 参数我们传的是 0，表示查询所有，并不是查询 0 条数据，（通过 SQL 的 LIMIT <Num>实现）
             * 后面的三级分类查询方式与二级分类的查询逻辑类似
             */
            List<GoodsCategory> secondLevelCategories = goodsCategoryMapper.
                    selectByLevelAndParentIdsAndNumber(firstLevelCategoryIds,
                            CategoryLevelEnum.LEVEL_TWO.getLevel(), 0);

//            判断二级分类的数据是否为非空，是则继续
            if (!CollectionUtils.isEmpty(secondLevelCategories)) {
//                获取二级分类数据中所有分类的 ID
                List<Long> secondLevelCategoryIds = secondLevelCategories.stream().
                        map(GoodsCategory::getCategoryId).collect(Collectors.toList());

//
                /*
                  通过获取到的二级分类数据中的 ID，获取对应 ID 中三级分类的数据。
                  因为上一步已经获取到所有的二级分类列表数据，所以我们把其中的 id 字段全部获取出来并放入到一个 List 对象中，
                  这里 parentIds 参数我们传的就是 secondLevelCategoryIds；
                  分类级别传的是 3，用的是枚举类 CategoryLevelEnum.LEVEL_THREE；
                  数量 number 参数我们传的是 0，表示查询所有，并不是查询 0 条数据，（通过 SQL 的 LIMIT <Num>实现）
                 */
                List<GoodsCategory> thirdLevelCategories = goodsCategoryMapper.
                        selectByLevelAndParentIdsAndNumber(secondLevelCategoryIds,
                                CategoryLevelEnum.LEVEL_THREE.getLevel(), 0);

//                判断三级分类的数据是否为非空，是则继续
                if (!CollectionUtils.isEmpty(thirdLevelCategories)) {
//                    此处注意，从最后一层开始处理获取到的分类数据
//                    根据 parentId 将 thirdLevelCategories 分组，并封装成 Map<K,V>
                    Map<Long, List<GoodsCategory>> thirdLevelCategoryMap = thirdLevelCategories.
                            stream().collect(groupingBy(GoodsCategory::getParentId));

                    List<SecondLevelCategoryVo> secondLevelCategoryVoList = new ArrayList<>();

//                    处理二级分类
                    for (GoodsCategory secondLevelCategory : secondLevelCategories) {

                        SecondLevelCategoryVo secondLevelCategoryVo = new SecondLevelCategoryVo();
//                        将 secondLevelCategory 深拷贝到 secondLevelCategoryVo，对应的 Vo 层数据返回到前端
                        BeanUtils.copyProperties(secondLevelCategory, secondLevelCategoryVo);

//                        如果该二级分类下有数据则放入 secondLevelCategoryVoList 对象中
                        if (thirdLevelCategoryMap.containsKey(secondLevelCategory.getCategoryId())) {

//                            根据二级分类的 id 取出 thirdLevelCategoryMap 分组中的三级分类的 List
//                            <K, V> 根据 Key(二级分类的 id) 获取 Value(三级分类的 List)
                            List<GoodsCategory> tempGoodsCategories = thirdLevelCategoryMap.
                                    get(secondLevelCategory.getCategoryId());

//                            深拷贝三级分类的 List 中 GoodsCategories 的数据到对应的 Vo 层对象
                            secondLevelCategoryVo.setThirdLevelCategoryVoList((BeanUtil.
                                    copyList(tempGoodsCategories, ThirdLevelCategoryVo.class)));
//                            将最终获取的二级分类 Vo 层对象封装成 List
                            secondLevelCategoryVoList.add(secondLevelCategoryVo);
                        }
                    }
//                    处理一级分类
                    if (!CollectionUtils.isEmpty(secondLevelCategoryVoList)) {

//                        根据 parentId 将 thirdLevelCategories 分组，并封装成 Map<K,V>
                        Map<Long, List<SecondLevelCategoryVo>> secondLevelCategoryVoMap =
                                secondLevelCategoryVoList.stream().collect(groupingBy(SecondLevelCategoryVo::getParentId));

//                        遍历一级分类数据中所有的商品分类信息，将其逐一深拷贝为 Vo 层对象
                        for (GoodsCategory firstCategory : firstLevelCategories) {

                            IndexCategoryVo indexCategoryVo = new IndexCategoryVo();
                            BeanUtils.copyProperties(firstCategory, indexCategoryVo);

//                            如果该一级分类下有数据则放入 indexCategoryVoList 对象中
                            if (secondLevelCategoryVoMap.containsKey(firstCategory.getCategoryId())) {

//                                根据一级分类的id取出secondLevelCategoryVOMap分组中的二级级分类list
                                List<SecondLevelCategoryVo> tempGoodsCategories = secondLevelCategoryVoMap.
                                        get(firstCategory.getCategoryId());

//                                深拷贝二级分类的 List 中 GoodsCategories 的数据到对应的 Vo 层对象
                                indexCategoryVo.setSecondLevelCategoryVoList(tempGoodsCategories);

//                                将最终获取的二级分类 Vo 层对象封装成 List
                                indexCategoryVoList.add(indexCategoryVo);
                            }
                        }
                    }
                }
            }
            return indexCategoryVoList;
        } else {
            return null;
        }
    }
}
