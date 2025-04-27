package com.mertcan.weatherapp.model;

import lombok.Data;

@Data
public class Weather {
    private double lon;
    private double lat;
    private String name;
    private double intensity;
    private double temperature;
    private String weather;
}