package com.zouj.api.web_auth.configs;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

    @Value("${zouj.openapi.dev-url}")
    private String devUrl;

    @Value("${zouj.openapi.prod-url}")
    private String prodUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearer-key";
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment");

        Server prodServer = new Server();
        prodServer.setUrl(prodUrl);
        prodServer.setDescription("Server URL in Production environment");

        Contact contact = new Contact();
        contact.setEmail("zouj@gmail.com");
        contact.setName("Jagger");
        contact.setUrl("https://ledevfullstack.com");

        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Authentication and Authorization Management API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage users authentication and authorization")
                .termsOfService("https://ledevfullstack.com")
                .license(mitLicense);

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer, prodServer))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(
                        new Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")));
    }

    // @Bean
    // public GroupedOpenApi publicApi() {
    // return GroupedOpenApi.builder()
    // .group("public")
    // .pathsToMatch("/**")
    // .build();
    // }
}
