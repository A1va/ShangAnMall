package com.shangan.mall.service;

import com.shangan.mall.controller.param.AdministratorUpdateParam;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @Author Alva
 * @CreateTime 2021/1/25 15:04
 */

public interface AdministratorService {
    /**
     * 用户注册
     *
     * @param loginName loginName
     * @param password password
     * @return String
     */
    String register(String loginName, String password);


    /**
     * 登录
     *
     * @param loginName loginName
     * @param passwordMD5 passwordMD5
     * @return String
     */
    String login(String loginName, String passwordMD5);

    /**
     * 用户信息修改
     * @param administrator
     * @return Boolean
     */
    Boolean updateAdministratorInfo(AdministratorUpdateParam administratorUpdateParam, Long administratorId);

    /**
     * 登出接口
     * @param administratorId
     * @return Boolean
     */
    Boolean logout(Long administratorId);
}
