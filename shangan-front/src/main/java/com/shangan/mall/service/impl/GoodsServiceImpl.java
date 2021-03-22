package com.shangan.mall.service.impl;

import com.shangan.mall.controller.vo.IndexConfigGoodsVo;
import com.shangan.mall.dao.GoodsMapper;
import com.shangan.mall.entity.Goods;
import com.shangan.mall.service.GoodsService;
import com.shangan.util.BeanUtil;
import com.shangan.util.PageQueryUtil;
import com.shangan.util.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Alva
 * @CreateTime 2021/1/29 20:56
 */
@Service("goodsService")
public class GoodsServiceImpl implements GoodsService {

    private final GoodsMapper goodsMapper;

    public GoodsServiceImpl(GoodsMapper goodsMapper) {
        this.goodsMapper = goodsMapper;
    }

    @Override
    public Goods getGoodsById(Long id) {
        return goodsMapper.selectByPrimaryKey(id);
    }

    /**
     *  searchNewBeeMallGoods() 方法并传入 PageQueryUtil 对象作为参数，商品类目 id 、关键字 keyword 字段、
     *  分页所需的 page 字段、排序字段等都作为属性放在了这个对象中，关键字或者商品类目 id 用来过滤想要的商品列表，
     *  page 用于确定查询第几页的数据，之后通过 SQL 查询出对应的分页数据，再之后是填充数据，某些字段太长会导致页面上的展示效果不好，
     *  所以对这些字段进行了简单的字符串处理并设置到 IndexConfigGoodsVo 对象中，最终返回的数据是 PageResult 对象，
     *  包含了当前页返回的商品列表数据还有分页信息
     * @param pageUtil
     * @return
     */
    @Override
    public PageResult searchGoods(PageQueryUtil pageUtil) {

        List<Goods> goodsList = goodsMapper.findGoodsListBySearch(pageUtil);
        int total = goodsMapper.getTotalGoodsBySearch(pageUtil);
        List<IndexConfigGoodsVo> indexConfigGoodsVoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(goodsList)) {
            indexConfigGoodsVoList = BeanUtil.copyList(goodsList, IndexConfigGoodsVo.class);
            for (IndexConfigGoodsVo indexConfigGoodsVo : indexConfigGoodsVoList) {
                String goodsName = indexConfigGoodsVo.getGoodsName();
                String goodsIntro = indexConfigGoodsVo.getGoodsIntro();
//                字符串过长导致文字超出的问题
                if (goodsName.length() > 28) {
                    goodsName = goodsName.substring(0, 28) + "...";
                    indexConfigGoodsVo.setGoodsName(goodsName);
                }
                if (goodsIntro.length() > 50) {
                    goodsIntro = goodsIntro.substring(0, 50) + "...";
                    indexConfigGoodsVo.setGoodsIntro(goodsIntro);
                }
            }
        }
        return (PageResult) new PageResult(indexConfigGoodsVoList, total, pageUtil.getLimit(), pageUtil.getPage());
    }
}
