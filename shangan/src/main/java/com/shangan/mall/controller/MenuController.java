package com.shangan.mall.controller;

import com.shangan.annotation.Token2Administrator;
import com.shangan.mall.entity.Administrator;
import com.shangan.mall.entity.Rights;
import com.shangan.mall.service.RightService;
import com.shangan.util.MenuUtil;
import com.shangan.util.Result;
import com.shangan.util.ResultGenerator;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/private/v1")
@Api(value = "v1", tags = "上岸商城后台主页菜单获取接口")
public class MenuController {

    @Resource
    private RightService rightService;

    @GetMapping("/menu")
    public Result<?> getMenu(@Token2Administrator Administrator administrator) {
        Rights rights = rightService.getRightById(administrator.getAdminUserId());
        if(rights != null) {
            return ResultGenerator.genSuccessResult(MenuUtil.getMenu(rights));
        }else {
            return ResultGenerator.genFailResult("");
        }
    }
}
