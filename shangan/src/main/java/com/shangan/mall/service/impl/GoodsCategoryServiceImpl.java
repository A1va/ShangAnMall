package com.shangan.mall.service.impl;

import com.shangan.common.CategoryLevelEnum;
import com.shangan.common.Constants;
import com.shangan.mall.controller.Vo.*;
import com.shangan.mall.controller.param.CategoryEditParam;
import com.shangan.mall.dao.GoodsCategoryMapper;
import com.shangan.mall.entity.Administrator;
import com.shangan.mall.entity.GoodsCategory;
import com.shangan.mall.service.GoodsCategoryService;
import com.shangan.util.BeanUtil;
import com.shangan.util.PageQueryUtil;
import com.shangan.util.PageResult;
import com.shangan.util.ResultGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service("goodsCategoryService")
public class GoodsCategoryServiceImpl implements GoodsCategoryService {

    private final GoodsCategoryMapper goodsCategoryMapper;

    public GoodsCategoryServiceImpl(GoodsCategoryMapper goodsCategoryMapper) {
        this.goodsCategoryMapper = goodsCategoryMapper;
    }

    @Override
    public PageResult getGoodsCategoryList(PageQueryUtil pageQueryUtil) {

        int totalNum = goodsCategoryMapper.getTotalCategoryNum(pageQueryUtil);
        List<GoodsCategory> goodsCategoryList = goodsCategoryMapper.selectGoodsCategoryList(pageQueryUtil);
        List<GoodsCategoryListVo> goodsCategoryListVos = new ArrayList<GoodsCategoryListVo>();
        if(goodsCategoryList != null) {
            goodsCategoryListVos = BeanUtil.copyList(goodsCategoryList, GoodsCategoryListVo.class);
        }
        System.out.println(goodsCategoryListVos);
        PageResult pageResult = new PageResult(goodsCategoryListVos, totalNum, pageQueryUtil.getLimit(), pageQueryUtil.getPage());
        return pageResult;
    }

    @Override
    public List<CategoryFirstVo> getAllCategoryList() {
        List<CategoryFirstVo> indexCategoryVoList = new ArrayList<>();

        /*
          获取一级分类中固定数量的数据。
          因为一级分类是没有父节点的，即父级分类的 id 为缺省值 0，同时 parentIds 参数为 List 类型，
          所以这里 parentIds 参数我们传的就是 Collections.singletonList(0L)；
          分类级别传的就是 1，这里用的是枚举类 CategoryLevelEnum.LEVEL_ONE；
          分类数量这里用的也是一个常量 Constants.INDEX_CATEGORY_NUMBER，默认为 10。
         */
        List<GoodsCategory> firstLevelCategories = goodsCategoryMapper.
                selectGoodsCategoryByParams(Collections.singletonList(0L),
                        CategoryLevelEnum.LEVEL_ONE.getLevel(), 0);

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
                    selectGoodsCategoryByParams(firstLevelCategoryIds,
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
                        selectGoodsCategoryByParams(secondLevelCategoryIds,
                                CategoryLevelEnum.LEVEL_THREE.getLevel(), 0);

//                判断三级分类的数据是否为非空，是则继续
                if (!CollectionUtils.isEmpty(thirdLevelCategories)) {
//                    此处注意，从最后一层开始处理获取到的分类数据
//                    根据 parentId 将 thirdLevelCategories 分组，并封装成 Map<K,V>
                    Map<Long, List<GoodsCategory>> thirdLevelCategoryMap = thirdLevelCategories.
                            stream().collect(groupingBy(GoodsCategory::getParentId));

                    List<CategorySecondVo> secondLevelCategoryVoList = new ArrayList<>();

//                    处理二级分类
                    for (GoodsCategory secondLevelCategory : secondLevelCategories) {

                        CategorySecondVo secondLevelCategoryVo = new CategorySecondVo();
//                        将 secondLevelCategory 深拷贝到 secondLevelCategoryVo，对应的 Vo 层数据返回到前端
                        BeanUtils.copyProperties(secondLevelCategory, secondLevelCategoryVo);

//                        如果该二级分类下有数据则放入 secondLevelCategoryVoList 对象中
                        if (thirdLevelCategoryMap.containsKey(secondLevelCategory.getCategoryId())) {

//                            根据二级分类的 id 取出 thirdLevelCategoryMap 分组中的三级分类的 List
//                            <K, V> 根据 Key(二级分类的 id) 获取 Value(三级分类的 List)
                            List<GoodsCategory> tempGoodsCategories = thirdLevelCategoryMap.
                                    get(secondLevelCategory.getCategoryId());

//                            深拷贝三级分类的 List 中 GoodsCategories 的数据到对应的 Vo 层对象
                            secondLevelCategoryVo.setChildren((BeanUtil.
                                    copyList(tempGoodsCategories, CategoryThirdVo.class)));
//                            将最终获取的二级分类 Vo 层对象封装成 List
                            secondLevelCategoryVoList.add(secondLevelCategoryVo);
                        }
                    }
//                    处理一级分类
                    if (!CollectionUtils.isEmpty(secondLevelCategoryVoList)) {

//                        根据 parentId 将 thirdLevelCategories 分组，并封装成 Map<K,V>
                        Map<Long, List<CategorySecondVo>> secondLevelCategoryVoMap =
                                secondLevelCategoryVoList.stream().collect(groupingBy(CategorySecondVo::getParentId));

//                        遍历一级分类数据中所有的商品分类信息，将其逐一深拷贝为 Vo 层对象
                        for (GoodsCategory firstCategory : firstLevelCategories) {

                            CategoryFirstVo indexCategoryVo = new CategoryFirstVo();
                            BeanUtils.copyProperties(firstCategory, indexCategoryVo);

//                            如果该一级分类下有数据则放入 indexCategoryVoList 对象中
                            if (secondLevelCategoryVoMap.containsKey(firstCategory.getCategoryId())) {

//                                根据一级分类的id取出secondLevelCategoryVOMap分组中的二级级分类list
                                List<CategorySecondVo> tempGoodsCategories = secondLevelCategoryVoMap.
                                        get(firstCategory.getCategoryId());

//                                深拷贝二级分类的 List 中 GoodsCategories 的数据到对应的 Vo 层对象
                                indexCategoryVo.setChildren(tempGoodsCategories);

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

    @Override
    public List<CategoryFirstVo> getParentCategoryList() {
        List<CategoryFirstVo> categoryFirstVoList = new ArrayList<CategoryFirstVo>();

        List<GoodsCategory> firstLevelCategories = goodsCategoryMapper.selectGoodsCategoryByParams(
                Collections.singletonList(0L), CategoryLevelEnum.LEVEL_ONE.getLevel(), 0);

        if(!CollectionUtils.isEmpty(firstLevelCategories)) {
            List<Long> firstLevelCategoryIds = firstLevelCategories.stream().
                    map(GoodsCategory::getCategoryId).collect(Collectors.toList());

            List<GoodsCategory> secondLevelCategories = goodsCategoryMapper.
                    selectGoodsCategoryByParams(firstLevelCategoryIds,
                            CategoryLevelEnum.LEVEL_TWO.getLevel(), 0);

            if (!CollectionUtils.isEmpty(secondLevelCategories)) {
                Map<Long, List<GoodsCategory>> secondLevelCategoryMap = secondLevelCategories.
                        stream().collect(groupingBy(GoodsCategory::getParentId));

                List<CategorySecondVo> categorySecondVoList = new ArrayList<>();

                categorySecondVoList = BeanUtil.copyList(secondLevelCategories, CategorySecondVo.class);

                if(!CollectionUtils.isEmpty(categorySecondVoList)) {
                    Map<Long, List<CategorySecondVo>> secondLevelCategoryVoMap =
                            categorySecondVoList.stream().collect(groupingBy(CategorySecondVo::getParentId));

                    for (GoodsCategory firstCategory : firstLevelCategories) {

                        CategoryFirstVo categoryFirstVo = new CategoryFirstVo();
                        BeanUtils.copyProperties(firstCategory, categoryFirstVo);

//                            如果该一级分类下有数据则放入 indexCategoryVoList 对象中
                        if (secondLevelCategoryVoMap.containsKey(firstCategory.getCategoryId())) {

//                                根据一级分类的id取出secondLevelCategoryVOMap分组中的二级级分类list
                            List<CategorySecondVo> tempGoodsCategories = secondLevelCategoryVoMap.
                                    get(firstCategory.getCategoryId());

//                                深拷贝二级分类的 List 中 GoodsCategories 的数据到对应的 Vo 层对象
                            categoryFirstVo.setChildren(tempGoodsCategories);

//                                将最终获取的二级分类 Vo 层对象封装成 List
                            categoryFirstVoList.add(categoryFirstVo);
                        }
                    }
                }
            }
            return categoryFirstVoList;
        }else {
            return null;
        }
    }

    @Override
    public Boolean addCategory(GoodsCategory goodsCategory) {
        return goodsCategoryMapper.addCategory(goodsCategory) > 0;
    }

    @Override
    public Boolean deleteCategory(Long categoryId) {
        return goodsCategoryMapper.deleteCategoryById(categoryId) > 0;
    }

    @Override
    public List<Long> getParentCategoryListById(Long categoryId, int type) {
        ParentCategoryListVo parentCategoryListVo = goodsCategoryMapper.selectParentIdList(categoryId, type);
        System.out.println(parentCategoryListVo);
        List<Long> parentIdList = new ArrayList<>();
        parentIdList.add(0,parentCategoryListVo.getFirstId());
        if(type == 3) {
            parentIdList.add(1,parentCategoryListVo.getSecondId());
        }
        return parentIdList;
    }

    @Override
    public Boolean editCategory(CategoryEditParam goodsEditParam, Administrator administrator) {
        GoodsCategory goodsCategory = new GoodsCategory();
        BeanUtils.copyProperties(goodsEditParam, goodsCategory);
        goodsCategory.setUpdateUser(administrator.getAdminUserId());
        goodsCategory.setUpdateTime(new Date());
        return goodsCategoryMapper.updateCategorySelective(goodsCategory) > 0;
    }
}
