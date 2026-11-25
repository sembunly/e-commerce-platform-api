package com.bunli.fashion.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
        @Bean
        OpenAPI fashionEcomOpenApi() {
                return new OpenAPI()
                                .info(new Info().title("Fashion E-commerce API").version("v1")
                                                .description("REST API documentation for the Fashion E-commerce platform."))
                                .components(new Components().addSecuritySchemes("bearerAuth",
                                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer")
                                                                .bearerFormat("JWT")));
        }
}
