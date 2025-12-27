package com.m01project.taskmanager.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI taskManagerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Task Manager API")
                        .description("API documentation for Task Manager project")
                        .version("v1.0")
                )
                .externalDocs(new ExternalDocumentation()
                        .description("Project GitHub repository")
                        .url("https://github.com/m01-project-devs/task-manager")
                );
    }
}
