package com.shangan.mall.service.impl;

import com.shangan.config.GlobalExceptionHandler;
import com.shangan.common.ServiceResultEnum;
import com.shangan.mall.controller.vo.UserAddressVo;
import com.shangan.mall.dao.UserAddressMapper;
import com.shangan.mall.entity.UserAddress;
import com.shangan.mall.service.UserAddressService;
import com.shangan.util.BeanUtil;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author Alva
 * @CreateTime 2021/2/1 18:59
 */
@Service("userAddressService")
public class UserAddressServiceImpl implements UserAddressService {

    private final UserAddressMapper userAddressMapper;

    public UserAddressServiceImpl(UserAddressMapper userAddressMapper) {
        this.userAddressMapper = userAddressMapper;
    }

    /**
     * 获取对应用户 Id 的用户地址
     * @param userId
     * @return
     */
    @Override
    public List<UserAddressVo> getMyAddresses(Long userId) {
        List<UserAddress> myAddressList = userAddressMapper.findMyAddressList(userId);
        return BeanUtil.copyList(myAddressList, UserAddressVo.class);
    }

    @Override
    public Boolean saveUserAddress(UserAddress UserAddress) {
        Date now = new Date();
        if (UserAddress.getDefaultFlag().intValue() == 1) {
//            添加默认地址，需要将原有的默认地址修改掉
            UserAddress defaultAddress = userAddressMapper.getMyDefaultAddress(UserAddress.getUserId());
            if (defaultAddress != null) {
                defaultAddress.setDefaultFlag((byte) 0);
                defaultAddress.setUpdateTime(now);
                int updateResult = userAddressMapper.updateByPrimaryKeySelective(defaultAddress);
                if (updateResult < 1) {
//                    未更新成功
                    GlobalExceptionHandler.fail(ServiceResultEnum.DB_ERROR.getResult());
                }
            }
        }
        return userAddressMapper.insertSelective(UserAddress) > 0;
    }

    @Override
    public Boolean updateUserAddress(UserAddress userAddress) {

        Date now = new Date();
        if (userAddress.getDefaultFlag().intValue() == 1) {
//            修改为默认地址，需要将原有的默认地址修改掉
            UserAddress defaultAddress = userAddressMapper.getMyDefaultAddress(userAddress.getUserId());
            if (defaultAddress != null) {
//                存在默认地址且默认地址并不是当前修改的地址
                defaultAddress.setDefaultFlag((byte) 0);
                defaultAddress.setUpdateTime(now);
                int updateResult = userAddressMapper.updateByPrimaryKeySelective(defaultAddress);
                if (updateResult < 1) {
//                    未更新成功
                    GlobalExceptionHandler.fail(ServiceResultEnum.DB_ERROR.getResult());
                }
            }
        }
        userAddress.setUpdateTime(now);
        return userAddressMapper.updateByPrimaryKeySelective(userAddress) > 0;
    }

    @Override
    public UserAddress getUserAddressById(Long addressId) {

        UserAddress userAddress = userAddressMapper.selectByPrimaryKey(addressId);
        if (userAddress == null) {
            GlobalExceptionHandler.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return userAddress;
    }

    @Override
    public UserAddress getMyDefaultAddressByUserId(Long userId) {
        return userAddressMapper.getMyDefaultAddress(userId);
    }

    @Override
    public Boolean deleteById(Long addressId) {
        return userAddressMapper.deleteByPrimaryKey(addressId) > 0;
    }
}
