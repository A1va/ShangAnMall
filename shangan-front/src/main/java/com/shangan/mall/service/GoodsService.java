package com.shangan.mall.service;

import com.shangan.mall.entity.Goods;
import com.shangan.util.PageQueryUtil;
import com.shangan.util.PageResult;

/**
 * @Author Alva
 * @CreateTime 2021/1/29 20:56
 */
public interface GoodsService {

    /**
     * 获取商品详情
     * @param id
     * @return
     */
    Goods getGoodsById(Long id);

    /**
     * 商品搜索
     * @param pageUtil
     * @return
     */
    PageResult<?> searchGoods(PageQueryUtil pageUtil);
}
