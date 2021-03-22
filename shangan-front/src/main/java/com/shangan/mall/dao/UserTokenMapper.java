package com.shangan.mall.dao;

import com.shangan.mall.entity.UserToken;
import org.springframework.stereotype.Repository;

/**
 * @Author Alva
 * @CreateTime 2021/1/26 20:53
 */
@Repository("userTokenMapper")
public interface UserTokenMapper {

    int deleteByPrimaryKey(Long userId);

    int insert(UserToken record);

    int insertSelective(UserToken record);

    UserToken selectByPrimaryKey(Long userId);

    UserToken selectByToken(String token);

    int updateByPrimaryKeySelective(UserToken record);

    int updateByPrimaryKey(UserToken record);
}
