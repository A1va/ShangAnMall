package com.shangan.config;

import java.lang.annotation.*;

/**
 * @Author Alva
 * @CreateTime 2021/1/27 11:59
 * 自定义 @Token2User 参数注解，使用注解和 AOP 方式将用户对象注入到方法中
 * 注解 @Target 接收参数
 * 注解 @Retention 注释将由编译器记录在类文件中，并在运行时由 VM 保留，因此可以通过反射方式读取它们
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Token2User {

    /**
     * 当前用户在request中的名字
     * @return
     */
    String value() default "user";
}
