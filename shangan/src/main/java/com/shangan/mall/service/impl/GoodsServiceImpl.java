package com.shangan.mall.service.impl;

import com.shangan.mall.controller.Vo.GoodsListVo;
import com.shangan.mall.controller.param.GoodsAddParam;
import com.shangan.mall.controller.param.GoodsEditParam;
import com.shangan.mall.dao.GoodsMapper;
import com.shangan.mall.entity.Administrator;
import com.shangan.mall.entity.Goods;
import com.shangan.mall.service.GoodsService;
import com.shangan.util.BeanUtil;
import com.shangan.util.PageQueryUtil;
import com.shangan.util.PageResult;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("goodsService")
public class GoodsServiceImpl implements GoodsService {

    private final GoodsMapper goodsMapper;

    public GoodsServiceImpl(GoodsMapper goodsMapper) {
        this.goodsMapper = goodsMapper;
    }


    @Override
    public PageResult getGoodsList(PageQueryUtil pageQueryUtil) {
//        List<GoodsListVo> goodsListVos = new ArrayList<GoodsListVo>();
//        List<Goods> goods = goodsMapper.selectAllGoods();
//        System.out.println(goods.get(1));
//        if(!goods.isEmpty()) {
//            System.out.println("123456");
//            goodsListVos = BeanUtil.copyList(goods, GoodsListVo.class);
//        }
        //获取商品总数
        int totalGoods = goodsMapper.getGoodsTotalNum(pageQueryUtil);
        List<GoodsListVo> goodsListVos = new ArrayList<GoodsListVo>();
        List<Goods> goods = goodsMapper.selectAllGoods(pageQueryUtil);

        if(totalGoods > 0) {
            goodsListVos = BeanUtil.copyList(goods, GoodsListVo.class);
        }
        PageResult pageResult = new PageResult(goods, totalGoods, pageQueryUtil.getLimit(), pageQueryUtil.getPage());
        return pageResult;
    }

    @Override
    public Boolean deleteGood(Long goodsId) {
        return goodsMapper.deleteGoodsById(goodsId) > 0;
    }

    @Override
    public Boolean addGoods(GoodsAddParam goodsAddParam, Administrator administrator) {
        Goods goods = new Goods();
        BeanUtils.copyProperties(goodsAddParam, goods);
        String detailContent = goods.getGoodsDetailContent();
        detailContent = detailContent.replace("<p>", "");
        detailContent = detailContent.replace("</p>", "");
        goods.setGoodsDetailContent(detailContent);
        goods.setGoodsSellStatus(Byte.parseByte("1"));
        goods.setCreateTime(new Date());
        goods.setCreateUser(administrator.getAdminUserId());
        goods.setSellingPrice(goodsAddParam.getOriginalPrice() + goodsAddParam.getProfit());
        goods.setGoodsCarousel(goodsAddParam.getGoodsCoverImg());
        return goodsMapper.insertGoodsSelective(goods) > 0;
    }

    @Override
    public Boolean editGoods(GoodsEditParam goodsEditParam, Administrator administrator) {
        Goods goods = new Goods();
        BeanUtils.copyProperties(goodsEditParam, goods);
        goods.setUpdateTime(new Date());
        goods.setUpdateUser(administrator.getAdminUserId());
        goods.setSellingPrice(goodsEditParam.getOriginalPrice() + goodsEditParam.getProfit());
        return goodsMapper.updateGoodsSelective(goods) > 0;
    }


}
