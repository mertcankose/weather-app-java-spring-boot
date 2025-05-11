package com.mertcan.weatherapp.controller;

import com.mertcan.weatherapp.model.Weather;
import com.mertcan.weatherapp.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/weather")
@Tag(name = "Weather", description = "Hava durumu işlemleri")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping
    @Operation(summary = "Hava durumu verilerini getir",
            description = "Güncel hava durumu verilerini API'den çeker",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Başarılı",
                    content = @Content(schema = @Schema(implementation = Weather.class))),
            @ApiResponse(responseCode = "401",
                    description = "Yetkisiz erişim",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Erişim engellendi",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Dış API hatası",
                    content = @Content)
    })
    public ResponseEntity<List<Weather>> getWeatherData() {
        List<Weather> weatherData = weatherService.fetchWeatherData();
        return ResponseEntity.ok(weatherData);
    }
}