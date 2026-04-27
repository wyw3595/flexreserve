package com.flexreserve.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {
    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("灵动预约（FlexReserve) API 接口文档")
                        .version("v1.0")
                        .description("FlexReserve 是一个基于SpringBoot的预约系统，提供预约、取消预约、查看预约等功能。")
                        .contact(new Contact().name("wyw"))

                );
    }
}
