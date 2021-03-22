package com.shangan.mall.controller;


import com.shangan.annotation.Token2Administrator;
import com.shangan.mall.entity.Administrator;
import com.shangan.mall.entity.User;
import com.shangan.mall.service.UserService;
import com.shangan.util.PageQueryUtil;
import com.shangan.util.Result;
import com.shangan.util.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/private/v1")
@Api(value = "v1", tags = "上岸商城用户管理相关接口")
public class UserController {

    private final UserService userService;

    private static final Logger logger = (Logger) LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/users")
    @ApiOperation(value = "锁定用户")
    public Result<?> addUser(@ApiParam(value = "用户ID") @RequestBody User user, @Token2Administrator Administrator administrator) {
        System.out.println(user);
        if(userService.addUser(user, administrator)) {
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult("");
        }
    }

    @DeleteMapping("/users/{id}")
    @ApiOperation(value = "删除用户接口")
    public Result<?> deleteUser(@PathVariable("id") @ApiParam(value = "用户编号") Long userId,
                                @Token2Administrator Administrator administrator) {
        //"login api,loginName={},loginResult={}", administratorLoginParam.getLoginName(), loginResult
        logger.info("lock api,adminloginName={} do delete userId={}", administrator.getNickName(), userId);
        if (userService.deleteUser(userId)) {
//            返回成功
            return ResultGenerator.genSuccessResult();
        } else {
//            返回失败
            return ResultGenerator.genFailResult("删除失败");
        }
    }

    //http://127.0.0.1:8888/api/private/v1/users?query=&pagenum=1&pagesize=2
    @GetMapping("/users")
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
        PageQueryUtil pageQueryUtil = new PageQueryUtil(params);
        System.out.println(pageQueryUtil);
        return ResultGenerator.genSuccessResult(userService.getUserInfoList(pageQueryUtil));
//
//        return null;
    }

    @PutMapping(value = "/users/{id}/state/{status}")
    @ApiOperation(value = "修改用户锁定状态接口")
    public Result<?> editUserStatus(@PathVariable("id") @ApiParam(value = "用户编号") Long categoryId,
                                    @PathVariable("status") @ApiParam(value = "用户状态") Boolean status,
                                    @Token2Administrator Administrator administrator) {
        if(userService.editUserStatus(categoryId, status, administrator)) {
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult("");
        }

    }

    @GetMapping(value = "/users/{id}")
    @ApiOperation(value = "获取某用户信息接口")
    public Result<?> editUserStatus(@PathVariable("id") @ApiParam(value = "用户编号") Long userId) {
        User user = userService.getUserInfoById(userId);
        System.out.println(user);
        if(user != null) {
            return ResultGenerator.genSuccessResult(user);
        }else {
            return ResultGenerator.genFailResult("");
        }

    }

    @PutMapping(value = "/users/{id}")
    @ApiOperation(value = "修改用户信息")
    public Result<?> editUserInfo(@PathVariable("id") @ApiParam(value = "用户编号") Long userId,
                                  @RequestBody User user,
                                  @Token2Administrator Administrator administrator) {
//        User user = userService.getUserInfoById(userId);
        user.setUserId(userId);
        if(userService.editUserInfoById(user, administrator)) {
            return ResultGenerator.genSuccessResult(user);
        }else {
            return ResultGenerator.genFailResult("");
        }

    }



}
