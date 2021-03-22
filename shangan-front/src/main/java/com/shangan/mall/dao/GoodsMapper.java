package com.shangan.mall.dao;

import com.shangan.mall.entity.Goods;
import com.shangan.mall.entity.StockNumDTO;
import com.shangan.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Alva
 * @CreateTime 2021/1/29 12:16
 */
@Repository("goodsMapper")
public interface GoodsMapper {

    int deleteByPrimaryKey(Long goodsId);

    int insert(Goods record);

    int insertSelective(Goods record);

    /**
     * 参数为商品 id 的列表，类型为 SELECT 语句，根据传入的商品主键列表，查询出所有的商品记录。
     * @param goodsId
     * @return
     */
    Goods selectByPrimaryKey(Long goodsId);

    int updateByPrimaryKeySelective(Goods record);

    int updateByPrimaryKeyWithBLOBs(Goods record);

    int updateByPrimaryKey(Goods record);

    List<Goods> findGoodsList(PageQueryUtil pageUtil);

    int getTotalGoods(PageQueryUtil pageUtil);

    List<Goods> selectByPrimaryKeys(List<Long> goodsIds);

    List<Goods> findGoodsListBySearch(PageQueryUtil pageUtil);

    int getTotalGoodsBySearch(PageQueryUtil pageUtil);

    int batchInsert(@Param("GoodsList") List<Goods> GoodsList);

    int updateStockNum(@Param("stockNumDTOS") List<StockNumDTO> stockNumDTOS);

    int batchUpdateSellStatus(@Param("orderIds")Long[] orderIds,
                              @Param("sellStatus") int sellStatus);
}
