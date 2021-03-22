package com.shangan.mall.controller;

import com.shangan.annotation.Token2Administrator;
import com.shangan.mall.controller.Vo.AdministratorVo;
import com.shangan.mall.controller.param.AdministratorLoginParam;
import com.shangan.common.Constants;
import com.shangan.mall.controller.param.AdministratorUpdateParam;
import com.shangan.mall.entity.Administrator;
import com.shangan.mall.service.AdministratorService;
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
import javax.validation.Valid;

/**
 * @Author Alva
 * @CreateTime 2021/1/25 14:53
 * @PostMapping("/user/login") 表示登录请求为 POST 方式，请求路径为 /api/v1/user/login，
 * @RequestBody 注解对登录参数进行接收并封装成 AdministratorLoginParam 对象，
 * @Valid 注解的作用为参数验证，定义参数时我们使用了 @NotEmpty 注解，
 * 表示该参数不能为空，如果在这里不添加 @Valid 注解，则非空验证不会执行。
 */
@RestController
@Api(value = "v1", tags = "上岸商城用户操作相关接口")
@RequestMapping("/api/private/v1")
public class AdministratorController {

    @Resource
    private AdministratorService administratorService;

    private static final Logger logger = LoggerFactory.getLogger(AdministratorController.class);

    @PostMapping("/admin/login")
    @ApiOperation(value = "登录接口", notes = "返回 token")
    public Result<?> login(@RequestBody @Valid AdministratorLoginParam administratorLoginParam) {
//        判断登录帐号是否为手机号格式

        String loginResult = administratorService.login(administratorLoginParam.getLoginName(), administratorLoginParam.getPasswordMd5());

        logger.info("login api,loginName={},loginResult={}", administratorLoginParam.getLoginName(), loginResult);

//        登录成功
        if (!StringUtils.isEmpty(loginResult)) {
            Result<String> result = (Result<String>) ResultGenerator.genSuccessResult();
            result.setData(loginResult);
            return result;
        }
//        登录失败
        return ResultGenerator.genFailResult(loginResult);
    }

    @PostMapping("/admin/register")
    @ApiOperation(value = "注册接口")
    public Result<?> register(@RequestParam("loginName") String loginName,@RequestParam("password") String password) {

        System.out.println(loginName);
        System.out.println(password);
        String registerResult = administratorService.register(loginName, password);
        return ResultGenerator.genFailResult(registerResult);

    }

    @PutMapping("/admin/info")
    @ApiOperation(value = "修改用户信息", notes = "")
    public Result<?> updateInfo(@RequestBody @ApiParam("管理员信息")AdministratorUpdateParam administratorUpdateParam, @Token2Administrator Administrator administrator) {
        Boolean flag = administratorService.updateAdministratorInfo(administratorUpdateParam, administrator.getAdminUserId());
        if (flag) {
//            返回成功
            Result result = ResultGenerator.genSuccessResult();
            return result;
        } else {
//            返回失败
            Result result = ResultGenerator.genFailResult("修改失败");
            return result;
        }
    }

    @PostMapping("/admin/logout")
    @ApiOperation(value = "注销接口", notes = "清除 token")
    public Result<?> logout(@Token2Administrator Administrator administrator) {
        boolean logoutResult = administratorService.logout(administrator.getAdminUserId());

        logger.info("logout api,loginMallAdmin={}", administrator.getAdminUserId());

        if(logoutResult) {
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult("logout error!");
    }

    @GetMapping("/admin/info")
    @ApiOperation(value = "获取管理员信息")
    public Result<?> getUserDetail(@Token2Administrator Administrator administrator) {
        AdministratorVo administratorVo = new AdministratorVo();
        BeanUtils.copyProperties(administrator, administratorVo);
        System.out.println(administrator);
        System.out.println(administratorVo);
        return ResultGenerator.genSuccessResult(administratorVo);

    }

}

