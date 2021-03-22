package com.shangan.mall.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shangan.mall.entity.Administrator;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Author Alva
 * @CreateTime 2021/1/25 23:18
 */
@Repository("administratorMapper")
public interface AdministratorMapper extends BaseMapper<Administrator> {
    int insert(Administrator record);

    int insertSelective(Administrator record);

    /**
     * 登陆方法
     *
     * @param loginName
     * @param password
     * @return
     */
    Administrator selectByLoginNameAndPasswd(@Param("loginName") String loginName,
                                             @Param("password") String password);

    Administrator selectByPrimaryKey(Long adminUserId);

    int updateByPrimaryKeySelective(Administrator record);

    int updateByPrimaryKey(Administrator record);
}
