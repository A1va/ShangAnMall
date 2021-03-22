package com.shangan.mall.service.impl;

import com.shangan.mall.controller.vo.IndexConfigGoodsVo;
import com.shangan.mall.dao.GoodsMapper;
import com.shangan.mall.dao.IndexConfigMapper;
import com.shangan.mall.entity.Goods;
import com.shangan.mall.entity.IndexConfig;
import com.shangan.mall.service.IndexConfigService;
import com.shangan.util.BeanUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Alva
 * @CreateTime 2021/1/29 12:11
 */
@Service("indexConfigService")
public class IndexConfigServiceImpl implements IndexConfigService {

    private final IndexConfigMapper indexConfigMapper;

    private final GoodsMapper goodsMapper;

    public IndexConfigServiceImpl(IndexConfigMapper indexConfigMapper, GoodsMapper goodsMapper) {
        this.indexConfigMapper = indexConfigMapper;
        this.goodsMapper = goodsMapper;
    }


    /**
     * 返回固定数量的首页配置商品对象(首页调用)
     * 首先根据 configType 参数读取固定数量的首页配置数据，之后获取配置项中关联的商品记录属性，
     * 然后对字符串进行处理并封装到 VO 对象里，之后设置到 request 域中，
     * 这里主要是将需要的数据读取出来并封装成一个对象返回给视图层。
     * @param configType
     * @param number
     * @return
     */
    @Override
    public List<IndexConfigGoodsVo> getConfigGoodsesForIndex(int configType, int number) {

        List<IndexConfigGoodsVo> indexConfigGoodsVoList = new ArrayList<>(number);
        List<IndexConfig> indexConfigList = indexConfigMapper.findIndexConfigsByTypeAndNum(configType, number);
        if (!CollectionUtils.isEmpty(indexConfigList)) {

//            取出所有的 goodsId
            List<Long> goodsIds = indexConfigList.stream().map(IndexConfig::getGoodsId).collect(Collectors.toList());
            List<Goods> lGoods = goodsMapper.selectByPrimaryKeys(goodsIds);
            indexConfigGoodsVoList = BeanUtil.copyList(lGoods, IndexConfigGoodsVo.class);
            for (IndexConfigGoodsVo IndexConfigGoodsVo : indexConfigGoodsVoList) {
                String goodsName = IndexConfigGoodsVo.getGoodsName();
                String goodsIntro = IndexConfigGoodsVo.getGoodsIntro();

//                字符串过长导致文字超出的问题
                if (goodsName.length() > 30) {
                    goodsName = goodsName.substring(0, 30) + "...";
                    IndexConfigGoodsVo.setGoodsName(goodsName);
                }
                if (goodsIntro.length() > 50) {
                    goodsIntro = goodsIntro.substring(0, 50) + "...";
                    IndexConfigGoodsVo.setGoodsIntro(goodsIntro);
                }
            }
        }
        return indexConfigGoodsVoList;
    }
}
