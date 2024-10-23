package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
                        .description("This API is part of the backend for a Hospital Information System (HIS) that manages patients, doctors, appointments, and diagnoses.\n\n"
                                + "### Roles and Permissions:\n\n"
                                + "- **Admin**: Manages users and has full access to the system. (Username: `admin`, Password: `admin`)\n"
                                + "- **Doctor**: Manages diagnoses and appointments for their own patients. (Username: `doctor`, Password: `doctor`)\n"
                                + "- **Manager**: Access to hospital metrics and data. (Username: `manager`, Password: `manager`)\n"
                                + "- **Patient**: Can view their own medical records and appointments. (Username: `patient`, Password: `patient`)\n\n"
                                + "### How to use the API:\n"
                                + "1. Use the `/auth/login` endpoint to authenticate. Submit a POST request with a JSON body containing your login and password.\n"
                                + "2. Upon successful login, you will receive a JWT token. This token should be included in the `Authorization` header for all subsequent API requests as `Bearer <JWT>`.\n"
                                + "3. Depending on your role, you will have different access levels to the API endpoints.\n\n"
                                + "### Important Endpoints:\n"
                                + "- `/auth/login`: Authenticate and receive a JWT token.\n"
                                + "- `/auth/register`: Register a new user (Admin only).\n"
                                + "- `/patients/{id}`: View or manage patients (Doctor/Admin).\n"
                                + "- `/appointments/{id}`: View or manage appointments (Doctor/Admin).\n"
                                + "- `/analytics`: View analytics (Manager/Admin).\n"
                        ))
                .components(new Components().addSecuritySchemes("bearerAuth", bearerAuth))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
