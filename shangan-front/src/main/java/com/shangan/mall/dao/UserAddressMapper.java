package com.shangan.mall.dao;

import com.shangan.mall.entity.UserAddress;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Alva
 * @CreateTime 2021/2/1 17:38
 */
@Repository
public interface UserAddressMapper {

    int deleteByPrimaryKey(Long addressId);

    int insert(UserAddress record);

    int insertSelective(UserAddress record);

    UserAddress selectByPrimaryKey(Long addressId);

    UserAddress getMyDefaultAddress(Long userId);

    List<UserAddress> findMyAddressList(Long userId);

    int updateByPrimaryKeySelective(UserAddress record);

    int updateByPrimaryKey(UserAddress record);
}
