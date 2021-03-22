package com.shangan.mall.dao;

import com.shangan.mall.entity.AdministratorToken;
import com.shangan.mall.entity.UserToken;
import org.springframework.stereotype.Repository;

@Repository("administratorTokenMapper")
public interface AdminstratorTokenMapper {


    int deleteByPrimaryKey(Long userId);

    int insert(AdministratorToken record);

    int insertSelective(AdministratorToken record);

    AdministratorToken selectByPrimaryKey(Long administratorId);

    AdministratorToken selectByToken(String token);

    int updateByPrimaryKeySelective(AdministratorToken record);

    int updateByPrimaryKey(AdministratorToken record);
}
