package com.mertcan.weatherapp.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Weather App API",
                version = "1.0",
                description = "Hava durumu uygulaması API dokümantasyonu"
        )
)
// @SecurityScheme(  // Bu kısmı comment'e alın
//         name = "bearerAuth",
//         type = SecuritySchemeType.HTTP,
//         bearerFormat = "JWT",
//         scheme = "bearer"
// )
public class OpenApiConfig {
}