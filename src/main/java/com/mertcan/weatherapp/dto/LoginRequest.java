package com.mertcan.weatherapp.dto;

import lombok.Data;

@Data  // Lombok anotasyonu, getter/setter metodlarını otomatik oluşturur
public class LoginRequest {
    private String username;
    private String password;
}