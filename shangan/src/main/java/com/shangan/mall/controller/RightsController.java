package com.shangan.mall.controller;

import com.shangan.annotation.Token2Administrator;
import com.shangan.mall.entity.Administrator;
import com.shangan.mall.service.RightService;
import com.shangan.util.PageQueryUtil;
import com.shangan.util.Result;
import com.shangan.util.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@Api(value = "v1", tags = "上岸商城权限管理相关接口")
@RequestMapping("/api/private/v1")
public class RightsController {

    @Resource
    private RightService rightService;

    @GetMapping("/rights")
    @ApiOperation(value = "获取权限列表接口", notes = "获取并分页")
    public Result<?> getGoodsList(@RequestParam(required = false) @ApiParam(value = "页码") Integer pagenum,
                                  @RequestParam(required = false) @ApiParam(value = "页大小") Integer pagesize) {

        Map params = new HashMap(5);
        if (pagenum == null || pagenum < 1) {
            pagenum = 1;
        }
//        params.put("query", query);
        params.put("page", pagenum);
        params.put("limit", pagesize);
        PageQueryUtil pageQueryUtil = new PageQueryUtil(params);
        System.out.println(pageQueryUtil);
        return ResultGenerator.genSuccessResult(rightService.getAllNormalRights(pageQueryUtil));
//
//        return null;
    }


    @PutMapping("/rights/{id}/management/{Management}/{type}")
    public Result<?> editUserStatus(@PathVariable("id") @ApiParam(value = "管理员编号") Long adminUserId,
                                    @PathVariable("Management") @ApiParam(value = "权限状态") Boolean Management,
                                    @PathVariable("type") @ApiParam(value = "修改权限标志") int type,
                                    @Token2Administrator Administrator administrator) {
        if(rightService.editAdminRights(adminUserId, Management, type)) {
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult("");
        }
    }

}
