package com.shangan.config;

import com.shangan.mall.entity.Administrator;
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
 * @CreateTime 2021/1/24 22:21
 * @Configuration   启动时加载此类作为配置类
 * @EnableSwagger2  表示此项目启用 Swagger API 文档
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    /**
     * 该方法用于返回实例 Docket（Swagger API 摘要），
     * 也是在该方法中指定需要扫描的控制器包路径，
     * 只有此路径下的 Controller 类才会自动生成 Swagger API 文档
     * @return Docket
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

        /*return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.shangan.mall.controller"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(swaggerParams);*/
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .ignoredParameterTypes(Administrator.class)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.shangan.mall.controller"))
                .paths(PathSelectors.any())
                .build()
//                最后将包含 token 的 swaggerParams 放入 globalOperationParameters 中
                .globalOperationParameters(swaggerParams);
    }

    /**
     * 该方法中主要是配置一些基础信息，包括配置页面展示的基本信息，
     * 标题、描述、版本、服务条款、联系方式等。
     * @return ApiInfo
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("上岸商城 api 接口文档")
                .description("swagger 接口文档")
                .version("1.0")
                .build();
    }
}
