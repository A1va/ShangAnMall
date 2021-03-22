package com.shangan.mall.service;

import com.shangan.mall.controller.param.UserUpdateParam;

/**
 * @Author Alva
 * @CreateTime 2021/1/26 20:05
 * 用户服务层接口
 */
public interface UserService {
    /**
     * 用户注册
     * @param loginName
     * @param password
     * @return
     */
    String register(String loginName, String password);


    /**
     * 登录
     * @param loginName
     * @param passwordMD5
     * @return
     */
    String login(String loginName, String passwordMD5);

    /**
     * 用户信息修改
     * @param mallUser
     * @return
     */
    Boolean updateUserInfo(UserUpdateParam mallUser, Long userId);

    /**
     * 登出接口
     * @param userId
     * @return
     */
    Boolean logout(Long userId);
}
