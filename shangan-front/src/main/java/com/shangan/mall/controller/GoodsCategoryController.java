package com.shangan.mall.controller;

import com.shangan.config.GlobalExceptionHandler;
import com.shangan.common.ServiceResultEnum;
import com.shangan.mall.controller.vo.IndexCategoryVo;
import com.shangan.mall.service.CategoryService;
import com.shangan.util.Result;
import com.shangan.util.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author Alva
 * @CreateTime 2021/1/29 16:33
 */
@RestController
@Api(value = "v1", tags = "上岸商城分类页面接口")
@RequestMapping("/api/v1")
public class GoodsCategoryController {

    private final CategoryService categoryService;

    public GoodsCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 方法名称为 getCategories()，请求类型为 GET，映射的路径为 /api/v1/categories，
     * 响应结果类型为 Result，实际的 data 属性类型为 IndexCategoryVO 的 List<> 对象，即分类列表数据。
     * 实现逻辑就是分别调用分类业务实现类 CategoryService 中的查询方法，将所需的数据查询并响应给前端，
     * 所有的实现逻辑都是在业务实现类中处理的，包括查询和字段设置，
     * 在控制层代码中只是将获得的数据结果设置到 Result 对象并返回。
     *
     * 分类页面的数据可以直接查看，并不需要登录认证，因此该接口中并没有使用权限认证的注解 @Token2User。
     * @return Result<List<IndexCategoryVo>>
     */
    @GetMapping("/categories")
    @ApiOperation(value = "获取分类数据", notes = "分类页面使用")
    public Result<List<IndexCategoryVo>> getCategories() {

        List<IndexCategoryVo> categoryVoList = categoryService.getCategoriesForIndex();
        if (CollectionUtils.isEmpty(categoryVoList)) {
            GlobalExceptionHandler.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return ResultGenerator.genSuccessResult(categoryVoList);
    }
}
