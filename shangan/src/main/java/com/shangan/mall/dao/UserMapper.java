package com.shangan.mall.dao;

import com.shangan.mall.entity.User;
import com.shangan.util.PageQueryUtil;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userMapper")
public interface UserMapper {

    Integer updateByPrimaryKeySelective(User record);

    Integer getUsersTotalNum(PageQueryUtil pageQueryUtil);

    List<User> selectAllUsers(PageQueryUtil pageQueryUtil);

//    Long userId, boolean status, Administrator administrator
    Integer updateUserStatus(@Param("userId") Long userId,
                             @Param("lockedFlag") Byte lockedFlag);

    User selectUserById(Long userId);

    Integer insertUserSelective(User user);

//    Integer updateUserInfo(User user);
}
