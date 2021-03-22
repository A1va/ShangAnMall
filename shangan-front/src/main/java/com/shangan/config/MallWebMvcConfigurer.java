package com.shangan.config;

import com.shangan.common.Constants;
import com.shangan.config.handler.Token2UserMethodArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @Author Alva
 * @CreateTime 2021/1/27 12:10
 */
@Configuration
public class MallWebMvcConfigurer implements WebMvcConfigurer {

    private final Token2UserMethodArgumentResolver token2UserMethodArgumentResolver;

    public MallWebMvcConfigurer(Token2UserMethodArgumentResolver token2UserMethodArgumentResolver) {
        this.token2UserMethodArgumentResolver = token2UserMethodArgumentResolver;
    }
    /**
     * Token2User 注解处理方法
     * @param argumentResolvers
     */
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(token2UserMethodArgumentResolver);
    }

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**").addResourceLocations("file:" + Constants.FILE_UPLOAD_DIC);
        registry.addResourceHandler("/goods-img/**").addResourceLocations("file:" + Constants.FILE_UPLOAD_DIC);
    }

    /**
     * 全局 CORS 跨域配置
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true).maxAge(3600);
    }
}
