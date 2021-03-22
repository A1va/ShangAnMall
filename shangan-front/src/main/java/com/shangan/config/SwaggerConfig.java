package com.shangan.config;

import com.shangan.mall.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Alva
 * @CreateTime 2021/1/26 17:51
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * 使用 ParameterBuilder 来定义 tokenParam，
     * @return
     */
    @Bean
    public Docket api() {
        ParameterBuilder tokenParam = new ParameterBuilder();
        List<Parameter> swaggerParams = new ArrayList<>();
        tokenParam.name("token").description("用户认证信息")
//                token 是一个字符串，且发送请求时是放在 Request Header 中
                .modelRef(new ModelRef("string")).parameterType("header")
//                header 中的 ticket 参数非必填，传空也可以
                .required(false).build();
//        根据每个方法名可知当前方法在设置什么参数
        swaggerParams.add(tokenParam.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .ignoredParameterTypes(User.class)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.shangan.mall.controller"))
                .paths(PathSelectors.any())
                .build()
//                最后将包含 token 的 swaggerParams 放入 globalOperationParameters 中
                .globalOperationParameters(swaggerParams);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("上岸商城接口文档")
                .description("swagger api 接口文档")
                .version("1.0")
                .build();
    }
}
