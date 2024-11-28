package ru.roh.springdemo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Task Management System API")
                        .version("1.0")
                        .description("API для управления задачами, пользователями и проектами")
                        .contact(new Contact()
                                .name("Mikhail")
                                .email("example@example.com")
                                .url("https://github.com/orphography/myproject")));
    }
}