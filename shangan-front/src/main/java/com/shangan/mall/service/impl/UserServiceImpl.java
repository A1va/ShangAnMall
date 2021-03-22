package com.shangan.mall.service.impl;

import com.shangan.common.Constants;
import com.shangan.config.GlobalExceptionHandler;
import com.shangan.common.ServiceResultEnum;
import com.shangan.mall.controller.param.UserUpdateParam;
import com.shangan.mall.dao.UserMapper;
import com.shangan.mall.dao.UserTokenMapper;
import com.shangan.mall.entity.User;
import com.shangan.mall.entity.UserToken;
import com.shangan.mall.service.UserService;
import com.shangan.util.JwtUtil;
import com.shangan.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @Author Alva
 * @CreateTime 2021/1/26 20:08
 * 用户服务层的实现类
 * 1.登录
 * 2.注册
 * 3.注销
 * 4.修改用户信息
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserTokenMapper userTokenMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, UserTokenMapper userTokenMapper) {
        this.userMapper = userMapper;
        this.userTokenMapper = userTokenMapper;
    }

    /**
     * 1.用户登录账号已存在，返回对应的错误信息(String)
     * 2.新建 User 对象接收用户登录帐号和其密码对应的 MD5 格式
     * 3.将 User 对象插入数据库，成功/失败返回对应的信息(String)
     * @param loginName 手机号
     * @param password 密码
     * @return
     */
    @Override
    public String register(String loginName, String password) {
//        手机号和密码的非空判断
        if (StringUtils.isEmpty(loginName) || StringUtils.isEmpty(password)){
            return ServiceResultEnum.LOGIN_ERROR.getResult();
        }
//        用户登录账号已存在
        if (userMapper.selectByLoginName(loginName) != null) {
            return ServiceResultEnum.SAME_LOGIN_NAME_EXIST.getResult();
        }
//        新建 User 对象接收用户信息
        User registerUser = new User();
        registerUser.setLoginName(loginName);
//        默认 nickName 为 loginName
        registerUser.setNickName(loginName);
//        设置用户介绍信息
        registerUser.setIntroduceSign(Constants.USER_INTRO);
//        生成密码对应的 MD5 码
        String passwordMD5 = MD5Util.MD5Encode(password, "UTF-8");
        registerUser.setPasswordMd5(passwordMD5);
//        将用户信息插入数据库
        if (userMapper.insertSelective(registerUser) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
//        数据库操作失败
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    /**
     * 登录或注册成功后，生成保持用户登录状态会话 token 值
     * 1.首先，根据用户名和密码查询用户数据，如果存在则继续后续流程
     * 2.判断用户状态是否正常，一切正常则继续后续流程
     * 3.生成 Token 值，可以简单的将其理解为生成一个随机字符串，在这一步其实已经完成了登录逻辑，只是后续需要对 Token 值进行查询，所以还需要将用户的 Token 信息插入数据库
     * 4.根据用户 id 查询 Token 信息表，以此结果来决定是进行 Token 更新操作/新增操作
     * 5.根据当前时间获取 Token 过期时间
     * 6.封装用户 Token 信息并进行数据库操作（新增/修改）
     * 7.最后，返回 Token 值
     * @param loginName  手机号
     * @param passwordMD5  MD5 加密后的密码
     * @return
     */
    @Override
    public String login(String loginName, String passwordMD5) {

//        手机号和密码的非空判断
        if (StringUtils.isEmpty(loginName) || StringUtils.isEmpty(passwordMD5)){
            return ServiceResultEnum.LOGIN_ERROR.getResult();
        }
//        判断用户的手机号和密码是否正确
        User user = userMapper.selectByLoginNameAndPasswd(loginName, passwordMD5);
//        如果用户存在
        if (user != null) {
//            判断用户是否已被锁，1:表示已被锁
            if (user.getLockedFlag() == 1) {
                return ServiceResultEnum.LOGIN_USER_LOCKED_ERROR.getResult();
            }
//            登录成功后，修改 token
            String jwtToken = JwtUtil.getJwtToken(user.getUserId(), user.getNickName());
//            新建/更新 UserToken 表的数据
            UserToken userToken = userTokenMapper.selectByPrimaryKey(user.getUserId());
//            当前时间
            Date now = new Date();
//            过期时间 = 当前时间 + 24 Hour
            Date expireTime = new Date(now.getTime() + JwtUtil.EXPIRE);
            if (userToken == null) {
                userToken = new UserToken();
                userToken.setUserId(user.getUserId());
                userToken.setToken(jwtToken);
                userToken.setUpdateTime(now);
                userToken.setExpireTime(expireTime);
//                新增一条token数据
                if (userTokenMapper.insertSelective(userToken) > 0) {
//                    新增成功后返回
                    return jwtToken;
                }
            } else {
                userToken.setToken(jwtToken);
                userToken.setUpdateTime(now);
                userToken.setExpireTime(expireTime);
//                更新
                if (userTokenMapper.updateByPrimaryKeySelective(userToken) > 0) {
//                    修改成功后返回
                    return jwtToken;
                }
            }
        }
        return ServiceResultEnum.LOGIN_ERROR.getResult();
    }

    @Override
    public Boolean updateUserInfo(UserUpdateParam User, Long userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            GlobalExceptionHandler.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        user.setNickName(user.getNickName());
//        user.setPasswordMd5(mallUser.getPasswordMd5());
//        若密码为空字符，则表明用户不打算修改密码，使用原密码保存
        if (!MD5Util.MD5Encode("", "UTF-8").equals(user.getPasswordMd5())){
            user.setPasswordMd5(user.getPasswordMd5());
        }
        user.setIntroduceSign(user.getIntroduceSign());
        if (userMapper.updateByPrimaryKeySelective(user) > 0) {
            return true;
        }
        return false;
    }

    /**
     * 注销: 删除用户 token 值
     * @param userId
     * @return
     */
    @Override
    public Boolean logout(Long userId) {
        return userTokenMapper.deleteByPrimaryKey(userId) > 0;
    }
}
