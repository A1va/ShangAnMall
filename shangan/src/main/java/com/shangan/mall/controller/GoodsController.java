package com.shangan.mall.controller;

import com.shangan.annotation.Token2Administrator;
import com.shangan.common.Constants;
import com.shangan.mall.controller.Vo.GoodsListVo;
import com.shangan.mall.controller.param.GoodsAddParam;
import com.shangan.mall.controller.param.GoodsEditParam;
import com.shangan.mall.entity.Administrator;
import com.shangan.mall.entity.Goods;
import com.shangan.mall.service.GoodsService;
import com.shangan.util.PageQueryUtil;
import com.shangan.util.Result;
import com.shangan.util.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "v1", tags = "上岸商城商品操作相关接口")
@RequestMapping("/api/private/v1")
public class GoodsController {
    /*http://127.0.0.1:8888/api/private/v1/goods?query=&pagenum=1&pagesize=10
    * */

    @Resource
    private GoodsService goodsService;

    @GetMapping("/goods")
    @ApiOperation(value = "获取商品列表接口", notes = "获取并分页")
    public Result<?> getGoodsList(@RequestParam(required = false) @ApiParam(value = "搜索关键字") String query,
                                  @RequestParam(required = false) @ApiParam(value = "页码") Integer pagenum,
                                  @RequestParam(required = false) @ApiParam(value = "页大小") Integer pagesize) {

        Map params = new HashMap(5);
        if (pagenum == null || pagenum < 1) {
            pagenum = 1;
        }
        params.put("query", query);
        params.put("page", pagenum);
        params.put("limit", pagesize);
        System.out.println(params);
        PageQueryUtil pageQueryUtil = new PageQueryUtil(params);
        System.out.println(pageQueryUtil);
        return ResultGenerator.genSuccessResult(goodsService.getGoodsList(pageQueryUtil));
//
//        return null;
    }

    @RequestMapping(value = "/goods/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除商品列表接口", notes = "根据ID删除商品条目")
    public Result<?> deleteGoods(@PathVariable("id") @ApiParam(value = "商品编号") Long goodsId) {
        if(goodsService.deleteGood(goodsId)) {
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult("");
        }
    }

//    http://127.0.0.1:8888/api/private/v1/goods
    @PostMapping("/goods")
    @ApiOperation(value = "添加商品列表接口")
    public Result<?> addGoods(@RequestBody @ApiParam("商品信息") GoodsAddParam goodsAddParam, @Token2Administrator Administrator administrator) {
        if(goodsService.addGoods(goodsAddParam, administrator)) {
            return ResultGenerator.genSuccessResult();
        }
        else {
            return ResultGenerator.genFailResult("");
        }
    }

    @PostMapping("/goods/edit")
    @ApiOperation(value = "修改商品信息接口")
    public Result<?> editGoods(@RequestBody @ApiParam("商品信息") GoodsEditParam goodsEditParam, @Token2Administrator Administrator administrator) {
        System.out.println(goodsEditParam);
        if(goodsService.editGoods(goodsEditParam, administrator)) {
            return ResultGenerator.genSuccessResult();
        }
        else {
            return ResultGenerator.genFailResult("");
        }
    }

}
