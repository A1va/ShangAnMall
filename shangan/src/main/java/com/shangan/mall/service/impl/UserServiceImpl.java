package com.shangan.mall.service.impl;

import com.shangan.mall.controller.Vo.GoodsListVo;
import com.shangan.mall.controller.Vo.UserManagerInfoVo;
import com.shangan.mall.dao.UserMapper;
import com.shangan.mall.entity.Administrator;
import com.shangan.mall.entity.Goods;
import com.shangan.mall.entity.User;
import com.shangan.mall.service.UserService;
import com.shangan.util.BeanUtil;
import com.shangan.util.PageQueryUtil;
import com.shangan.util.PageResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }


    @Override
    public Boolean lockUser(Long userId) {

        User user = new User();
        user.setUserId(userId);
        user.setLockedFlag(Byte.parseByte("1"));
        if(userMapper.updateByPrimaryKeySelective(user) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean deleteUser(Long userId) {
        User user = new User();
        user.setUserId(userId);
        user.setIsDeleted(Byte.parseByte("1"));
        if(userMapper.updateByPrimaryKeySelective(user) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public PageResult getUserInfoList(PageQueryUtil pageQueryUtil) {
        //获取商品总数
        int totalUsers = userMapper.getUsersTotalNum(pageQueryUtil);
        List<UserManagerInfoVo> userManagerInfoVos = new ArrayList<UserManagerInfoVo>();
        List<User> user = userMapper.selectAllUsers(pageQueryUtil);

        if(totalUsers > 0) {
            userManagerInfoVos = BeanUtil.copyList(user, UserManagerInfoVo.class);
            userManagerInfoVos.forEach((v)->{
                if(v.getLockedFlag() == 0) {
                    v.setStatus(true);
                }else {
                    v.setStatus(false);
                }
            });
        }
        PageResult pageResult = new PageResult(userManagerInfoVos, totalUsers, pageQueryUtil.getLimit(), pageQueryUtil.getPage());
        return pageResult;

    }

    @Override
    public User getUserInfoById(Long userId) {
        return userMapper.selectUserById(userId);
    }

    @Override
    public Boolean editUserStatus(Long userId, boolean status, Administrator administrator) {
        Byte lockedFlag;
        if(status == true) {
            lockedFlag = 0;
        }else {
            lockedFlag = 1;
        }
        return userMapper.updateUserStatus(userId, lockedFlag) > 0;
    }

    @Override
    public Boolean editUserInfoById(User user, Administrator administrator) {
        return userMapper.updateByPrimaryKeySelective(user) > 0;
    }

    @Override
    public Boolean addUser(User user, Administrator administrator) {
        user.setIsDeleted(Byte.parseByte("0"));
        user.setLockedFlag(Byte.parseByte("0"));
        user.setCreateTime(new Date());
        return userMapper.insertUserSelective(user) > 0;
    }

}
