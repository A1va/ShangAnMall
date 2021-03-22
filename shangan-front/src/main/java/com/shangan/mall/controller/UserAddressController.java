package com.shangan.mall.controller;

import com.shangan.common.ServiceResultEnum;
import com.shangan.config.Token2User;
import com.shangan.mall.controller.param.SaveUserAddressParam;
import com.shangan.mall.controller.param.UpdateUserAddressParam;
import com.shangan.mall.controller.vo.UserAddressVo;
import com.shangan.mall.entity.User;
import com.shangan.mall.entity.UserAddress;
import com.shangan.mall.service.UserAddressService;
import com.shangan.util.Result;
import com.shangan.util.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author Alva
 * @CreateTime 2021/2/1 19:12
 */
@RestController
@Api(value = "v1", tags = "上岸商城个人地址相关接口")
@RequestMapping("/api/v1")
public class UserAddressController {

    private final UserAddressService userAddressService;

    public UserAddressController(UserAddressService userAddressService) {
        this.userAddressService = userAddressService;
    }

    @GetMapping("/address")
    @ApiOperation(value = "我的收货地址列表", notes = "")
    public Result<List<UserAddressVo>> addressList(@Token2User User loginMallUser) {

        return ResultGenerator.genSuccessResult(userAddressService.getMyAddresses(loginMallUser.getUserId()));
    }

    @PostMapping("/address")
    @ApiOperation(value = "添加地址", notes = "")
    public Result<Boolean> saveUserAddress(@RequestBody SaveUserAddressParam saveMallUserAddressParam,
                                           @Token2User User loginMallUser) {
        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(saveMallUserAddressParam, userAddress);
        userAddress.setUserId(loginMallUser.getUserId());
        Boolean saveResult = userAddressService.saveUserAddress(userAddress);
//        添加成功
        if (saveResult) {
            return ResultGenerator.genSuccessResult();
        }
//        添加失败
        return ResultGenerator.genFailResult("添加失败");
    }

    // 修改地址与删除地址： post请求
    @PostMapping("/address/update")
//    @PutMapping("/address")
    @ApiOperation(value = "修改地址", notes = "")
    public Result<Boolean> updateMallUserAddress(@RequestBody UpdateUserAddressParam updateMallUserAddressParam,
                                                 @Token2User User loginMallUser) {
        UserAddress userAddressById = userAddressService.getUserAddressById(updateMallUserAddressParam.getAddressId());
        if (!loginMallUser.getUserId().equals(userAddressById.getUserId())) {
            return ResultGenerator.genFailResult(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(updateMallUserAddressParam, userAddress);
        userAddress.setUserId(loginMallUser.getUserId());
        Boolean updateResult = userAddressService.updateUserAddress(userAddress);
        //修改成功
        if (updateResult) {
            return ResultGenerator.genSuccessResult();
        }
        //修改失败
        return ResultGenerator.genFailResult("修改失败");
    }

    @GetMapping("/address/{addressId}")
    @ApiOperation(value = "获取收货地址详情", notes = "传参为地址id")
    public Result<UserAddressVo> getMallUserAddress(@PathVariable("addressId") Long addressId,
                                                              @Token2User User loginUser) {

        UserAddress userAddressById = userAddressService.getUserAddressById(addressId);
        UserAddressVo newBeeUserAddressVO = new UserAddressVo();
        BeanUtils.copyProperties(userAddressById, newBeeUserAddressVO);
        if (!loginUser.getUserId().equals(userAddressById.getUserId())) {
            return ResultGenerator.genFailResult(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        return ResultGenerator.genSuccessResult(newBeeUserAddressVO);
    }

    @GetMapping("/address/default")
    @ApiOperation(value = "获取默认收货地址", notes = "无传参")
    public Result getDefaultUserAddress(@Token2User User loginUser) {

        UserAddress userAddressById = userAddressService.getMyDefaultAddressByUserId(loginUser.getUserId());
        return ResultGenerator.genSuccessResult(userAddressById);
    }

    // 改为get 进行测试
    @GetMapping("/address/delete")
    @ApiOperation(value = "删除收货地址", notes = "传参为地址id")
    public Result deleteAddress(Long addressId, @Token2User User loginUser) {

        UserAddress userAddressById = userAddressService.getUserAddressById(addressId);
        if (!loginUser.getUserId().equals(userAddressById.getUserId())) {
            return ResultGenerator.genFailResult(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        System.out.println(addressId);
        Boolean deleteResult = userAddressService.deleteById(addressId);
//        删除成功
        if (deleteResult) {
            return ResultGenerator.genSuccessResult();
        }
//        删除失败
        return ResultGenerator.genFailResult(ServiceResultEnum.OPERATE_ERROR.getResult());
    }
}
