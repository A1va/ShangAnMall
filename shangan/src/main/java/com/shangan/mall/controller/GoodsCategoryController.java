package com.shangan.mall.controller;

import com.shangan.annotation.Token2Administrator;
import com.shangan.common.GlobalExceptionHandler;
import com.shangan.common.ServiceResultEnum;
import com.shangan.mall.controller.Vo.CategoryFirstVo;
import com.shangan.mall.controller.param.CategoryEditParam;
import com.shangan.mall.controller.param.GoodsEditParam;
import com.shangan.mall.entity.Administrator;
import com.shangan.mall.entity.GoodsCategory;
import com.shangan.mall.service.GoodsCategoryService;
import com.shangan.util.PageQueryUtil;
import com.shangan.util.Result;
import com.shangan.util.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@Api(value = "v1", tags = "上岸商城分类页面接口")
@RequestMapping("/api/private/v1")
public class GoodsCategoryController {

    private final GoodsCategoryService goodsCategoryService;

    public GoodsCategoryController(GoodsCategoryService goodsCategoryService) {
        this.goodsCategoryService = goodsCategoryService;
    }

    @GetMapping("/categories")
    @ApiOperation(value = "获取分类数据", notes = "分类列表使用")
    public Result<?> getGoodsCategoryList(@RequestParam(required = false) @ApiParam("获取类型") Integer type,
                                         @RequestParam(required = false) @ApiParam(value = "页码") Integer pagenum,
                                         @RequestParam(required = false) @ApiParam(value = "页大小") Integer pagesize) {
        //System.out.println(type);
        if(type == 3) {
            Map params = new HashMap(5);
            if (pagenum == null || pagenum < 1) {
                pagenum = 1;
            }
            params.put("page", pagenum);
            params.put("limit", pagesize);
            System.out.println(params);
            PageQueryUtil pageQueryUtil = new PageQueryUtil(params);
            return ResultGenerator.genSuccessResult(goodsCategoryService.getGoodsCategoryList(pageQueryUtil));
        }
        else if(type == 2) {
//            System.out.println();
            return ResultGenerator.genSuccessResult(goodsCategoryService.getParentCategoryList());
        }
       else {
            return ResultGenerator.genSuccessResult(goodsCategoryService.getAllCategoryList());
        }
    }

    @PostMapping("/categories")
    @ApiOperation(value = "添加分类")
    public Result<?> addCategory(@RequestBody @Validated GoodsCategory goodsCategory, @Token2Administrator Administrator administrator) {
        goodsCategory.setCategoryRank(0);
        goodsCategory.setIsDeleted(Byte.parseByte("0"));
        goodsCategory.setCreateTime(new Date());
        goodsCategory.setCreateUser(administrator.getAdminUserId());
        if(goodsCategoryService.addCategory(goodsCategory)) {
            return ResultGenerator.genSuccessResult();
        }
        else {
            List<CategoryFirstVo> categoryFirstVoList = goodsCategoryService.getAllCategoryList();
            if (CollectionUtils.isEmpty(categoryFirstVoList)) {
                GlobalExceptionHandler.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
            }
            return ResultGenerator.genSuccessResult(categoryFirstVoList);
            //return  ResultGenerator.genFailResult("添加分类失败！");
        }
    }

    @RequestMapping(value = "/categories/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除分类列表接口", notes = "根据ID删除分类条目")
    public Result<?> deleteCategory(@PathVariable("id") @ApiParam(value = "分类编号") Long categoryId) {
        if(goodsCategoryService.deleteCategory(categoryId)) {
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult("");
        }

    }

    @GetMapping(value = "/categories/{id}")
    @ApiOperation(value = "获取某分类的父级分类接口")
    public Result<?> getParentCategoryList(@PathVariable("id") @ApiParam(value = "分类编号") Long categoryId,
                                           @RequestParam @ApiParam("获取类型") Integer type) {
            return ResultGenerator.genSuccessResult(goodsCategoryService.getParentCategoryListById(categoryId,type));
    }

    @PostMapping("/categories/edit")
    @ApiOperation(value = "修改分类信息接口")
    public Result<?> editCategory(@RequestBody @ApiParam("商品信息") CategoryEditParam categoryEditParam, @Token2Administrator Administrator administrator) {
        System.out.println(categoryEditParam);
//        goodsCategoryService.editCategory(categoryEditParam, administrator);
        if(goodsCategoryService.editCategory(categoryEditParam, administrator)) {
            return ResultGenerator.genSuccessResult();
        }
        else {
            return ResultGenerator.genFailResult("");
        }
    }
}
