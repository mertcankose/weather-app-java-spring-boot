package com.mertcan.weatherapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor  // Tüm parametrelerle constructor
public class JwtResponse {
    private String token;
}