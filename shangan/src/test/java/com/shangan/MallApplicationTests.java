package com.shangan;

import com.shangan.common.ServiceResultEnum;
import com.shangan.mall.dao.AdministratorMapper;
import com.shangan.mall.entity.Administrator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MallApplicationTests {

    @Autowired
    private AdministratorMapper administratorMapper;

    @Test
    void contextLoads() {
//        参数是一个 wrapper 条件构造器，先不用
//        查询全部用户
        System.out.println(ServiceResultEnum.LOGIN_ERROR.getResult());
    }

}
