package com.shangan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.shangan.mall.dao")
@SpringBootApplication
public class ShanganApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShanganApplication.class, args);
    }

}
