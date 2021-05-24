package com.eareiza.springAngular.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration  {
	
	   /**
     * Publish a bean to generate swagger2 endpoints
        * https://programmer.ink/think/using-swagger-to-test-the-interface-how-do-i-carry-token-in-the-request-header.html
     * @return a swagger configuration bean
     */
	 @Bean
     public Docket api() {
         return new Docket(DocumentationType.SWAGGER_2)
             .select()
             .apis(RequestHandlerSelectors.basePackage("com.eareiza.springAngular.controller"))
             .paths(PathSelectors.any())
             .build()
             .securityContexts(Arrays.asList(securityContexts()))
             .securitySchemes(Arrays.asList(securitySchemes()))
             .apiInfo(new ApiInfoBuilder()
                 .description("Documentacion de Servicios.")
                 .title("Servicios comerciales")
                 .contact(new Contact("eareiza","http://66.228.61.76/kiosco","dirielfran@gmail.com"))
                 .version("v1.0")
                 .license("Apache2.0")
                 .build());
	 }

    private SecurityScheme securitySchemes() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

    private SecurityContext securityContexts() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.any())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("xxx", "Descriptive Information");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("Authorization", authorizationScopes));
    }


}
