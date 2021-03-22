package com.shangan.mall.controller;

import com.shangan.common.Constants;
import com.shangan.common.ServiceResultEnum;
import com.shangan.config.Token2User;
import com.shangan.mall.controller.param.UserLoginParam;
import com.shangan.mall.controller.param.UserRegisterParam;
import com.shangan.mall.controller.param.UserUpdateParam;
import com.shangan.mall.controller.vo.UserVo;
import com.shangan.mall.entity.User;
import com.shangan.mall.service.UserService;
import com.shangan.util.NumberUtil;
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

import javax.validation.Valid;

/**
 * @Author Alva
 * @CreateTime 2021/1/26 20:15
 */
@RestController
@Api(value = "v1", tags = "上岸商城用户操作相关 api 接口")
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 1.用 UserRegisterParam 类来接收用户注册的参数(登录帐号和密码)
     * 2.用户登录账号不是手机号码格式则返回对应错误信息
     * 3.调用 service 层的注册接口，实现对数据库的插入操作
     * 4.返回处理结果
     * @param userRegisterParam
     * @return
     */
    @PostMapping("/user/register")
    @ApiOperation(value = "用户注册")
    public Result<?> register(@RequestBody @Valid UserRegisterParam userRegisterParam) {
//        用户登录账号不是手机号码格式则返回对应错误信息
        if (NumberUtil.isNotPhone(userRegisterParam.getLoginName())){
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_NAME_IS_NOT_PHONE.getResult());
        }
        String registerResult = userService.register(userRegisterParam.getLoginName(), userRegisterParam.getPassword());

        logger.info("register api,loginName={},loginResult={}", userRegisterParam.getLoginName(), registerResult);

//        注册成功(数据库操作)
        if (ServiceResultEnum.SUCCESS.getResult().equals(registerResult)) {
            return ResultGenerator.genSuccessResult();
        }
//        注册失败
        return ResultGenerator.genFailResult(registerResult);
    }

    /**
     * 注解 @PostMapping("/user/login") 表示登录请求为 POST 方式，请求路径为 /api/v1/user/login，
     * 首先使用 @RequestBody 注解对登录参数进行接收并封装成 UserLoginParam 对象，
     * 注解 @Valid 的作用为参数验证，定义参数时我们使用了 @NotEmpty 注解，表示该参数不能为空，
     * 如果在这里不添加 @Valid 注解，则非空验证不会执行
     * @param userLoginParam
     * @return
     */
    @PostMapping("/user/login")
    @ApiOperation(value = "登录接口", notes = "返回 token")
    public Result<String> login(@RequestBody @Valid UserLoginParam userLoginParam) {
//        判断登录帐号
        if (NumberUtil.isNotPhone(userLoginParam.getLoginName())){
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_NAME_IS_NOT_PHONE.getResult());
        }
//        返回 token 值，使用 JWT 生成
        String loginResult = userService.login(userLoginParam.getLoginName(), userLoginParam.getPasswordMd5());
//        登录成功
        if (!StringUtils.isEmpty(loginResult)&& loginResult.length() > Constants.TOKEN_RESULT_LENGTH) {
            Result result = ResultGenerator.genSuccessResult();
            result.setData(loginResult);
            return result;
        }
//        登录失败
        return ResultGenerator.genFailResult(loginResult);
    }

    /**
     * 这个接口的逻辑比较简单，只需要将该用户在 token 表中的记录删除掉即可，也就是将当前的 token 值设置为无效的，
     * 既然注销了肯定不能让当前的 token 值可以继续进行身份验证。
     * @param loginUser
     * @return
     */
    @PostMapping("/user/logout")
    @ApiOperation(value = "注销接口", notes = "清除 token")
    public Result<String> logout(@Token2User User loginUser) {
        Boolean logoutResult = userService.logout(loginUser.getUserId());

        logger.info("logout api,loginMallUser={}", loginUser.getUserId());

//        注销成功
        if (logoutResult) {
            return ResultGenerator.genSuccessResult();
        }
//        注销失败
        return ResultGenerator.genFailResult("logout error");
    }

    /**
     * 定义了 UserUpdateParam 对象来接收用户修改的信息字段，
     * 需要修改的字段主要有昵称、密码、个性签名，并使用 @RequestBody 注解来接收
     * ，之后调用业务层的 updateUserInfo() 方法来进行入库操作，将这些字段 update 掉。
     * @param userUpdateParam
     * @param loginUser
     * @return
     */
    @PutMapping("/user/info")
    @ApiOperation(value = "修改用户信息", notes = "")
    public Result<?> updateInfo(@RequestBody @ApiParam("用户信息") UserUpdateParam userUpdateParam, @Token2User User loginUser) {
        Boolean flag = userService.updateUserInfo(userUpdateParam, loginUser.getUserId());
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

    /**
     * 使用 @Token2User 注解 已经得到了当前登录的用户对象 User，因此不用再去数据库中查询，
     * 直接返回即可，不过这里并没有直接返回 User 对象，而是新定义了一个 UserVo 对象，
     * 因为 User 对象字段较多，但是有些字段前端用不到，所以就定义了 UserVo 对象，
     * 返回前端所需要的字段(3个)即可。
     * @param loginUser
     * @return
     */
    @GetMapping("/user/info")
    @ApiOperation(value = "获取用户信息")
    public Result<UserVo> getUserDetail(@Token2User User loginUser) {
//        为了保护数据库的安全性/响应速度，只返回需要的属性
        UserVo userVO = new UserVo();
//        将已登录的 user 拷贝到 userVO
        BeanUtils.copyProperties(loginUser, userVO);
        return ResultGenerator.genSuccessResult(userVO);
    }
}
