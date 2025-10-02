package com.calc.rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("RESTful Calculator API")
                .description("A microservices-based calculator API that supports basic arithmetic operations " +
                        "(addition, subtraction, multiplication, division) with arbitrary precision for two operands.")
                .version("1.0.0"));    
    }
}