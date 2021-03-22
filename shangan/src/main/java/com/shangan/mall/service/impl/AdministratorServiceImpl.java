package com.shangan.mall.service.impl;

import com.shangan.common.GlobalExceptionHandler;
import com.shangan.common.ServiceResultEnum;
import com.shangan.mall.controller.param.AdministratorUpdateParam;
import com.shangan.mall.dao.AdministratorMapper;
import com.shangan.mall.dao.AdminstratorTokenMapper;
import com.shangan.mall.entity.Administrator;
import com.shangan.mall.entity.AdministratorToken;
import com.shangan.mall.service.AdministratorService;
import com.shangan.util.JwtUtil;
import com.shangan.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @Author Alva
 * @CreateTime 2021/1/25 17:54
 */
@Service("administratorService")
public class AdministratorServiceImpl implements AdministratorService {
//    空实现
//    @Override
//    public String register(String loginName, String password) {
//        return null;
//    }


    private final AdministratorMapper administratorMapper;
    private final AdminstratorTokenMapper adminstratorTokenMapper;

    @Autowired
    public AdministratorServiceImpl(AdministratorMapper administratorMapper, AdminstratorTokenMapper adminstratorTokenMapper) {
        this.administratorMapper = administratorMapper;
        this.adminstratorTokenMapper = adminstratorTokenMapper;
    }

    @Override
    public String login(String loginName, String passwordMD5) {

        if(StringUtils.isEmpty(loginName) || StringUtils.isEmpty(passwordMD5)) {
            return ServiceResultEnum.LOGIN_ERROR.getResult();
        }

        Administrator administrator = administratorMapper.selectByLoginNameAndPasswd(loginName, passwordMD5);

        if(administrator != null) {
            if(administrator.getLocked() == 1) {
                return ServiceResultEnum.LOGIN_USER_LOCKED_ERROR.getResult();
            }
        }

        String jwtToken = JwtUtil.getJwtToken(administrator.getAdminUserId(), administrator.getNickName());

        AdministratorToken administratorToken =  adminstratorTokenMapper.selectByPrimaryKey(administrator.getAdminUserId());

        Date now = new Date();
        if(administratorToken == null) {
            administratorToken = new AdministratorToken(
                    administrator.getAdminUserId(),
                    jwtToken,
                    now,
                    new Date(now.getTime() + JwtUtil.EXPIRE)
            );
            if(adminstratorTokenMapper.insertSelective(administratorToken) > 0) {
                return jwtToken;
            }
        } else {
            System.out.println("hhhh");
            administratorToken.setToken(jwtToken);
            administratorToken.setUpdateTime(now);
            administratorToken.setExpireTime(new Date(now.getTime() + JwtUtil.EXPIRE));
            if (adminstratorTokenMapper.updateByPrimaryKeySelective(administratorToken) > 0) {
//                    修改成功后返回
                return jwtToken;
            }
        }
        return ServiceResultEnum.LOGIN_ERROR.getResult();
    }

    @Override
    public String register(String loginName, String password) {

        Administrator administrator = new Administrator();
        administrator.setLoginUserName(loginName);
        administrator.setNickName(loginName);
        System.out.println(administrator);
        String passwordMD5 = MD5Util.MD5Encode(password, "UTF-8");
        System.out.println(administrator);
        administrator.setLoginPassword(passwordMD5);
        System.out.println(passwordMD5);
        System.out.println(administrator);
        administratorMapper.insertSelective(administrator);


        return ServiceResultEnum.SUCCESS.getResult();
    }

    @Override
    public Boolean updateAdministratorInfo(AdministratorUpdateParam administratorUpdateParam, Long administratorId) {

        Administrator administrator = administratorMapper.selectByPrimaryKey(administratorId);
        if(administrator == null) {
            GlobalExceptionHandler.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        administrator.setNickName(administratorUpdateParam.getNickName());
        if(!MD5Util.MD5Encode("","UTF-8").equals(administratorUpdateParam.getPasswordMd5())) {
            administrator.setLoginPassword(administratorUpdateParam.getPasswordMd5());
        }
        if(administratorMapper.updateByPrimaryKeySelective(administrator) > 0) {
            return true;
        }
        return false;
    }



    @Override
    public Boolean logout(Long administratorId) {
        return adminstratorTokenMapper.deleteByPrimaryKey(administratorId) > 0;

    }
}
