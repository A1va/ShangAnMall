package com.shangan.mall.service;

import com.shangan.mall.controller.vo.IndexConfigGoodsVo;

import java.util.List;

/**
 * @Author Alva
 * @CreateTime 2021/1/29 12:09
 */
public interface IndexConfigService {

    /**
     * 返回固定数量的首页配置商品对象(首页调用)
     * @param number
     * @return
     */
    List<IndexConfigGoodsVo> getConfigGoodsesForIndex(int configType, int number);
}
