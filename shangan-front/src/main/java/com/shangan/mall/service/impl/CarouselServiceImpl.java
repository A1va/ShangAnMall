package com.shangan.mall.service.impl;

import com.shangan.mall.controller.vo.IndexCarouselVo;
import com.shangan.mall.dao.CarouselMapper;
import com.shangan.mall.entity.Carousel;
import com.shangan.mall.service.CarouselService;
import com.shangan.util.BeanUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Alva
 * @CreateTime 2021/1/28 13:46
 */
@Service("carouselService")
public class CarouselServiceImpl implements CarouselService {

    private final CarouselMapper carouselMapper;

    public CarouselServiceImpl(CarouselMapper carouselMapper) {
        this.carouselMapper = carouselMapper;
    }

    @Override
    public List<IndexCarouselVo> getCarouselsForIndex(int number) {
        List<IndexCarouselVo> carouselVoList = new ArrayList<>(number);
        List<Carousel> carouselList = carouselMapper.findCarouselsByNum(number);
        if (!CollectionUtils.isEmpty(carouselList)) {
            carouselVoList = BeanUtil.copyList(carouselList, IndexCarouselVo.class);
        }
        return carouselVoList;
    }


}
