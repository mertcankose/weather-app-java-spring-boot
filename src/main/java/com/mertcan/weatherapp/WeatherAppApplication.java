package com.mertcan.weatherapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class WeatherAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherAppApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")  // Tüm originlere izin ver
                        .allowedMethods("*")  // Tüm HTTP methodlarına izin ver
                        .allowedHeaders("*")  // Tüm headerlara izin ver
                        .exposedHeaders("*")  // Tüm headerlara client erişimi
                        .maxAge(3600);        // Preflight yanıtlarının önbelleğe alınma süresi

                // Not: allowCredentials(true) kullanmak için allowedOrigins("*") yerine
                // belirli originler belirtilmelidir. Bu yüzden kaldırıldı.
            }
        };
    }
}