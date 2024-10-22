package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.configuration.security;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.Components;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        SecurityScheme bearerAuth = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        return new OpenAPI()
                .info(new Info()
                        .title("Mini-SINA API by César")
                        .version("v1")
                        .description("This API is part of the backend for a Hospital Information System (HIS) that manages patients, doctors, appointments, and diagnoses. The supported roles are: \n\n" +
                                "- **Admin**: Manages users and has full access to the system.\n" +
                                "- **Manager**: Access to hospital metrics and data.\n" +
                                "- **Doctor**: Manages diagnoses and appointments for their own patients.\n\n" +
                                "To log in, users need to send their credentials to the `mini-sina/v1/auth/login` endpoint. The system will respond with a JWT token, which must be included in each API request using the `Bearer` scheme. The token should be added to the `Authorization` header as `Bearer <JWT>` to authenticate all requests in Swagger and the API."))
                .components(new Components().addSecuritySchemes("bearerAuth", bearerAuth))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}

