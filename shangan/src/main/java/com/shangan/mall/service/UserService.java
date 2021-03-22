package com.shangan.mall.service;

import com.shangan.mall.entity.Administrator;
import com.shangan.mall.entity.User;
import com.shangan.util.PageQueryUtil;
import com.shangan.util.PageResult;

public interface UserService {

    Boolean lockUser(Long userId);

    Boolean deleteUser(Long userId);

    PageResult getUserInfoList(PageQueryUtil pageQueryUtil);

    User getUserInfoById(Long userId);

    Boolean editUserStatus(Long userId, boolean status, Administrator administrator);

    Boolean editUserInfoById(User user, Administrator administrator);

    Boolean addUser(User user, Administrator administrator);
}
