package com.mertcan.weatherapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WeatherAppApplication {

    public static void main(String[] args) {
        System.out.println("=== STARTING WEATHER APP ===");

        // System properties yazdır
        System.out.println("Java version: " + System.getProperty("java.version"));
        System.out.println("Spring Boot version: " + SpringApplication.class.getPackage().getImplementationVersion());

        try {
            SpringApplication app = new SpringApplication(WeatherAppApplication.class);
            app.setLazyInitialization(true); // Lazy initialization aktif et
            app.run(args);

            System.out.println("=== APPLICATION STARTED SUCCESSFULLY ===");
        } catch (Exception e) {
            System.err.println("=== APPLICATION FAILED TO START ===");
            e.printStackTrace();
        }
    }
}