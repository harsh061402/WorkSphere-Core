package com.harshkumar0614jain.worksphere.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                .title("WorkSphere API")
                .version("1.0.0")
                .description("WorkSphere is a production-grade Employee Management System REST API " +
                        "built with Spring Boot and MongoDB. It supports User Management, " +
                        "Employee Management, Leave Allocation and Leave Request Management.")
                .contact(new Contact()
                        .name("Harsh Kumar Jain")
                        .email("harshkumar0614jain@gmail.com")));
    }
}
