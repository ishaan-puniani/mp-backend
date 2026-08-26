package com.mp.be.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    public static final String SECURITY_SCHEME_NAME = "BearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Manufacturing Platform Backend API")
                        .description("Production & Process Management APIs")
                        .version("1.0.0"))
                .servers(List.of(
                        new Server().url("/").description("Default Server")
                ))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter your JWT authorization token: Bearer <token>")
                        )
                );
    }

    @Bean
    public GroupedOpenApi manufacturingApi() {
        return GroupedOpenApi.builder()
                .group("1-Manufacturing-Core")
                .displayName("1. Manufacturing & Plant Operations")
                .pathsToMatch(
                        "/api/tenant/*/process-configuration/**",
                        "/api/tenant/*/material/**",
                        "/api/tenant/*/machine/**",
                        "/api/tenant/*/product/**"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi accessApi() {
        return GroupedOpenApi.builder()
                .group("2-Auth-Access")
                .displayName("2. Authentication & Access")
                .pathsToMatch(
                        "/api/auth/**"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("3-Tenant-Admin")
                .displayName("3. Tenant & System Administration")
                .pathsToMatch(
                        "/api/tenant/**",
                        "/api/tenant/*/user/**",
                        "/api/tenant/*/file/**",
                        "/api/tenant/*/audit-log/**",
                        "/api/tenant/*/settings/**"
                )
                .pathsToExclude(
                        "/api/tenant/*/process-configuration/**",
                        "/api/tenant/*/material/**",
                        "/api/tenant/*/machine/**",
                        "/api/tenant/*/product/**"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi allApi() {
        return GroupedOpenApi.builder()
                .group("4-All-APIs")
                .displayName("4. All Endpoints (Complete Overview)")
                .pathsToMatch("/api/**")
                .build();
    }
}
