package com.shangan.mall.dao;

import com.shangan.mall.entity.Goods;
import com.shangan.util.PageQueryUtil;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("goodsMapper")
public interface GoodsMapper {

    Integer getGoodsTotalNum(PageQueryUtil pageQueryUtil);

    List<Goods> selectAllGoods(PageQueryUtil pageQueryUtil);

    Integer deleteGoodsById(Long goodsId);

    //int insertSelective(GoodsCategory record);
    Integer insertGoodsSelective(Goods goods);

    Integer updateGoodsSelective(Goods goods);


}
