package com.shangan.mall.service;

import com.shangan.mall.controller.vo.UserAddressVo;
import com.shangan.mall.entity.UserAddress;

import java.util.List;

/**
 * @Author Alva
 * @CreateTime 2021/2/1 17:53
 */
public interface UserAddressService {

    /**
     * 获取我的收货地址
     * @param userId
     * @return
     */
    List<UserAddressVo> getMyAddresses(Long userId);

    /**
     * 保存收货地址
     * @param userAddress
     * @return
     */
    Boolean saveUserAddress(UserAddress userAddress);

    /**
     * 修改收货地址
     * @param userAddress
     * @return
     */
    Boolean updateUserAddress(UserAddress userAddress);

    /**
     * 获取收货地址详情
     * @param addressId
     * @return
     */
    UserAddress getUserAddressById(Long addressId);

    /**
     * 获取我的默认收货地址
     * @param userId
     * @return
     */
    UserAddress getMyDefaultAddressByUserId(Long userId);

    /**
     * 删除收货地址
     * @param addressId
     * @return
     */
    Boolean deleteById(Long addressId);
}
