package com.shangan.mall.controller;

import com.shangan.common.Constants;
import com.shangan.common.IndexConfigTypeEnum;
import com.shangan.mall.controller.vo.IndexCarouselVo;
import com.shangan.mall.controller.vo.IndexConfigGoodsVo;
import com.shangan.mall.controller.vo.IndexInfoVo;
import com.shangan.mall.service.CarouselService;
import com.shangan.mall.service.IndexConfigService;
import com.shangan.util.Result;
import com.shangan.util.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author Alva
 * @CreateTime 2021/1/29 12:35
 */
@RestController
@Api(value = "v1", tags = "上岸商城首页接口")
@RequestMapping("/api/v1")
public class IndexController {

    private final CarouselService carouselService;

    private final IndexConfigService indexConfigService;

    public IndexController(CarouselService carouselService, IndexConfigService indexConfigService) {
        this.carouselService = carouselService;
        this.indexConfigService = indexConfigService;
    }

    /**
     * 方法名称为 indexInfo()，请求类型为 GET，映射的路径为 /api/v1/index-infos，响应结果类型为 Result，实际的 data 属性类型为 IndexInfoVO 对象
     * 实现逻辑就是分别调用轮播图业务实现类 CarouselService 中的查询方法和首页配置业务员实现类 IndexConfigService 中的查询方法，
     * 将所需的数据查询到并且设置到 IndexInfoVO 对象中，最后响应给前端，因为商品推荐有热销商品、新品上线、推荐商品三种类型，
     * 所以 getConfigGoodsesForIndex() 方法也调用了三次，只是每次传入的参数并不相同。
     * @return
     */
    @GetMapping("/index-infos")
    @ApiOperation(value = "获取首页数据", notes = "轮播图、新品、推荐等")
    public Result<IndexInfoVo> indexInfo() {

        IndexInfoVo indexInfoVo = new IndexInfoVo();

//        因为商品推荐有热销商品、新品上线、推荐商品三种类型，所以 getConfigGoodsesForIndex() 方法也调用了三次，只是每次传入的参数并不相同。
//        分别调用轮播图业务实现类 CarouselService 中的查询方法和首页配置业务员实现类 IndexConfigService 中的查询方法，
        List<IndexCarouselVo> carousels = carouselService.getCarouselsForIndex(Constants.INDEX_CAROUSEL_NUMBER);
        List<IndexConfigGoodsVo> hotGoodses = indexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_HOT.getType(), Constants.INDEX_GOODS_HOT_NUMBER);
        List<IndexConfigGoodsVo> newGoodses = indexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_NEW.getType(), Constants.INDEX_GOODS_NEW_NUMBER);
        List<IndexConfigGoodsVo> recommendGoodses = indexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_RECOMMOND.getType(), Constants.INDEX_GOODS_RECOMMOND_NUMBER);

//        将所需的数据查询到并且设置到 IndexInfoVO 对象中
        indexInfoVo.setCarousels(carousels);
        indexInfoVo.setHotGoodses(hotGoodses);
        indexInfoVo.setNewGoodses(newGoodses);
        indexInfoVo.setRecommendGoodses(recommendGoodses);

        return ResultGenerator.genSuccessResult(indexInfoVo);
    }
}
