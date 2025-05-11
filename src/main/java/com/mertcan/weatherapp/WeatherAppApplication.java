package com.mertcan.weatherapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class WeatherAppApplication {

    @EventListener
    public void onApplicationEvent(ApplicationFailedEvent event) {
        System.err.println("=== APPLICATION FAILED ===");
        System.err.println("Error: " + event.getException().getMessage());
        event.getException().printStackTrace();
    }

    @EventListener
    public void onApplicationReady(ApplicationReadyEvent event) {
        System.out.println("=== APPLICATION READY ===");
    }

    public static void main(String[] args) {
        System.out.println("=== STARTING WEATHER APP ===");

        try {
            SpringApplication app = new SpringApplication(WeatherAppApplication.class);

            // Fail-fast etkinleştir
            app.setRegisterShutdownHook(false);
            app.run(args);

            System.out.println("=== APP STARTED SUCCESSFULLY ===");
        } catch (Exception e) {
            System.err.println("=== STARTUP FAILED ===");
            e.printStackTrace();
            System.exit(1);
        }
    }
}