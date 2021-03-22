package com.shangan.mall.service;

import com.shangan.mall.controller.Vo.GoodsListVo;
import com.shangan.mall.controller.param.GoodsAddParam;
import com.shangan.mall.controller.param.GoodsEditParam;
import com.shangan.mall.entity.Administrator;
import com.shangan.mall.entity.Goods;
import com.shangan.util.PageQueryUtil;
import com.shangan.util.PageResult;

import java.util.List;

public interface GoodsService {

    PageResult getGoodsList(PageQueryUtil pageQueryUtil);

    Boolean deleteGood(Long goodsId);
    //Integer getGoodTotalNum();

    Boolean addGoods(GoodsAddParam goodsAddParam, Administrator administrator);

    Boolean editGoods(GoodsEditParam goodsEditParam, Administrator administrator);


}
