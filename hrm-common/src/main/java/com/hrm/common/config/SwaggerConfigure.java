package com.hrm.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Description Swagger配置类
 * @Author LZL
 * @Date 2022/1/13-16:36
 */
@Configuration
@EnableSwagger2
public class SwaggerConfigure {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.hrm"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        //作者信息
        final Contact contact = new Contact(
                "李正良",
                "https://lizhengliang1226.github.io",
                "1731415266@qq.com");
        return new ApiInfoBuilder()
                .title("HRM管理系统API文档")
                .description("海纳百川，有容乃大")
                .version("1.0")
                .termsOfServiceUrl("https://lizhengliang1226.github.io")
                .contact(contact).build();
    }
}
