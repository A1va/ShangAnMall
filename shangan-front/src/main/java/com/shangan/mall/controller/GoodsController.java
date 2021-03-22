package com.shangan.mall.controller;

import com.shangan.common.Constants;
import com.shangan.config.GlobalExceptionHandler;
import com.shangan.common.ServiceResultEnum;
import com.shangan.mall.controller.vo.GoodsDetailVo;
import com.shangan.mall.controller.vo.IndexConfigGoodsVo;
import com.shangan.mall.entity.Goods;
import com.shangan.mall.service.GoodsService;
import com.shangan.util.PageQueryUtil;
import com.shangan.util.PageResult;
import com.shangan.util.Result;
import com.shangan.util.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @Author Alva
 * @CreateTime 2021/1/29 21:57
 */
@RestController
@Api(value = "v1", tags = "上岸商城商品相关接口")
@RequestMapping("/api/v1")
public class GoodsController {
    private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Resource
    private GoodsService GoodsService;

    /**
     * 实现逻辑就是调用商品业务实现类 GoodsService 中的查询方法，
     * 将所需的数据查询并响应给前端，所有的实现逻辑都是在业务实现类中处理的，
     * 包括查询和返回字段的内容设置，在控制层代码中，主要是进行参数判断和参数的封装，
     * 之后将获得的数据结果设置到 Result 对象并返回。
     *
     * @param keyword 参数是关键字，用来过滤商品名和商品简介
     * @param goodsCategoryId 参数是用来过滤商品分类 id 的字段
     * @param orderBy 则是排序字段，传过来不同的排序方式，返回的数据也会不同
     * @param pageNumber 参数是分页所必需的字段，如果不传的话默认为第 1 页
     * @param loginUser 参数是当前登陆用户的信息，因为需要对用户进行权限认证，所以用 @TokenToMallUser 注解来接收
     * @return 根据以上字段进行查询参数的封装，之后通过 SQL 查询出对应的分页数据 pageResult
     *  data 属性类型为 PageResult< SearchGoodsVo-List > 对象，即商品搜索结果的分页列表数据
     */
    @GetMapping("/search")
    @ApiOperation(value = "商品搜索接口", notes = "根据关键字和分类id进行搜索")
    public Result<PageResult<List<IndexConfigGoodsVo>>> search(@RequestParam(required = false) @ApiParam(value = "搜索关键字") String keyword,
                                                               @RequestParam(required = false) @ApiParam(value = "分类 id") Long goodsCategoryId,
                                                               @RequestParam(required = false) @ApiParam(value = "orderBy") String orderBy,
                                                               @RequestParam(required = false) @ApiParam(value = "页码") Integer pageNumber,
                                                               @RequestParam(required = false) @ApiParam(value = "页大小") Integer pageSize) {
//        param 5: @Token2User User loginUser  身份认证
        logger.info("goods search api, keyword={}, goodsCategoryId={}, orderBy={}, pageNumber={}", keyword, goodsCategoryId, orderBy, pageNumber);
//        logger.info("goods search api,keyword={},goodsCategoryId={},orderBy={},pageNumber={},userId={}", keyword, goodsCategoryId, orderBy, pageNumber, loginUser.getUserId());

//        通过 Map 对象封装搜索参数 (商品 Id、页数、单页的商品数量)
        HashMap params = new HashMap(5);

//        两个搜索参数都为空，直接返回搜索异常
        if (goodsCategoryId == null && StringUtils.isEmpty(keyword)) {
            GlobalExceptionHandler.fail("非法的搜索参数");
        }
//        分页结果只有一页
        if (pageNumber == null || pageNumber < 1) {
            pageNumber = 1;
        }
//        将商品的分类 Id、页数、单页的商品数封装到 Map
        params.put("goodsCategoryId", goodsCategoryId);
        params.put("page", pageNumber);
//        params.put("limit", Constants.GOODS_SEARCH_PAGE_LIMIT);
        params.put("limit", pageSize);

//        对 keyword 做过滤，去掉空格
        if (!StringUtils.isEmpty(keyword)) {
            params.put("keyword", keyword);
        }
//        去掉 orderBy 的空格
        if (!StringUtils.isEmpty(orderBy)) {
            params.put("orderBy", orderBy);
        }
//        搜索上架状态下的商品
        params.put("goodsSellStatus", Constants.SELL_STATUS_UP);

//        通过 PageQueryUtil 类，封装商品数据
        PageQueryUtil pageUtil = new PageQueryUtil(params);
//        带 PageQueryUtil 调用 Service 层的搜索接口，并返回 ResultGenerator 生成的结果
        return ResultGenerator.genSuccessResult(GoodsService.searchGoods(pageUtil));
    }

    /**
     * 将查询到的商品详情数据转换为 GoodsDetailVo 对象并返回给前端。商品表中并不是所有的字段都需要返回，
     * 所以通过新建Vo层 GoodsDetailVo 类，
     * @param goodsId 参数就是商品主键 id，通过 @PathVariable 注解读取路径中的这个字段值，
     *                并根据这个值去调用 GoodsService 中的 getGoodsById() 方法获取到 Goods 对象，
     *                getGoodsById() 方法的实现方式就是根据主键 id 去查询数据库中的商品表并返回商品实体数据
     * @param loginUser
     * @return
     */
    @GetMapping("/goods/detail")
    @ApiOperation(value = "商品详情接口", notes = "传参为商品 id")
    public Result<GoodsDetailVo> goodsDetail(@RequestParam(required = false) @ApiParam(value = "搜索关键字") Long goodsId) {
//        param2: @Token2User User loginUser
        logger.info("goods detail api, goodsId={}", goodsId);
//        logger.info("goods detail api, goodsId={}, userId={}", goodsId, loginUser.getUserId());
//        判断商品的 Id 是否符合规范
        if (goodsId < 1) {
            return ResultGenerator.genFailResult("参数异常");
        }
//        通过商品的 Id 查询出商品的信息
        Goods goods = GoodsService.getGoodsById(goodsId);
//        判空
        if (goods == null) {
            return ResultGenerator.genFailResult("参数异常");
        }
//        若商品不是上架状态，返回 "商品已下架"(GOODS_PUT_DOWN) 异常
        if (Constants.SELL_STATUS_UP != goods.getGoodsSellStatus()) {
            GlobalExceptionHandler.fail(ServiceResultEnum.GOODS_PUT_DOWN.getResult());
        }
//        将获取的商品信息通过深拷贝封装到 GoodsDetailVo 对象
        GoodsDetailVo goodsDetailVo = new GoodsDetailVo();
        BeanUtils.copyProperties(goods, goodsDetailVo);
//        其中，GoodsDetailVo 商品详情 VO 层的 GoodsCarouselList 列表需要通过
        goodsDetailVo.setGoodsCarouselList(goods.getGoodsCarousel().split(","));
        return ResultGenerator.genSuccessResult(goodsDetailVo);
    }

}
