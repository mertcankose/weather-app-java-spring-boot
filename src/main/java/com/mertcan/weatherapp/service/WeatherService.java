package com.mertcan.weatherapp.service;

import com.mertcan.weatherapp.dto.OpenWeatherResponse;
import com.mertcan.weatherapp.model.Weather;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

@Service
public class WeatherService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${openweather.api.key}")
    private String apiKey;

    @Value("${openweather.api.url}")
    private String apiUrl;

    // Turkish cities to fetch weather data for
    private final List<String> turkishCities = Arrays.asList(
            "Istanbul", "Ankara", "Izmir", "Denizli", "Adana",
            "Erzurum", "Antalya", "Kayseri", "Bursa", "Gaziantep", "Trabzon"
    );

    public List<Weather> fetchWeatherData() {
        List<Weather> weatherDataList = new ArrayList<>();

        for (String city : turkishCities) {
            try {
                // Construct the API URL with city name and API key
                String url = apiUrl + "?q=" + city + ",tr&units=metric&appid=" + apiKey;

                // Make API call to OpenWeatherMap
                OpenWeatherResponse response = restTemplate.getForObject(url, OpenWeatherResponse.class);

                if (response != null) {
                    Weather weatherData = mapToWeatherData(response);
                    weatherDataList.add(weatherData);
                }
            } catch (Exception e) {
                // Log the error but continue with other cities
                System.err.println("Error fetching weather data for " + city + ": " + e.getMessage());
            }
        }

        return weatherDataList;
    }

    private Weather mapToWeatherData(OpenWeatherResponse response) {
        Weather weatherData = new Weather();

        // Map coordinates
        weatherData.setLon(response.getCoord().getLon());
        weatherData.setLat(response.getCoord().getLat());

        // Map city name
        weatherData.setName(response.getName());

        // Map temperature (already in Celsius due to 'units=metric' parameter)
        weatherData.setTemperature(response.getMain().getTemp());

        // Map weather condition
        String mainWeather = response.getWeather().get(0).getMain().toLowerCase();

        // Set weather type based on OpenWeatherMap condition
        if (mainWeather.contains("rain") || mainWeather.contains("drizzle")) {
            weatherData.setWeather("rainy");
            // Calculate intensity based on description or humidity
            weatherData.setIntensity(calculateIntensity(response));
        } else if (mainWeather.contains("cloud")) {
            weatherData.setWeather("cloudy");
            weatherData.setIntensity(response.getMain().getHumidity() / 100.0);
        } else if (mainWeather.contains("clear")) {
            weatherData.setWeather("sunny");
            weatherData.setIntensity(0.2); // Low intensity for sunny weather
        } else if (mainWeather.contains("snow")) {
            weatherData.setWeather("snowy");
            weatherData.setIntensity(0.6);
        } else if (mainWeather.contains("thunderstorm")) {
            weatherData.setWeather("stormy");
            weatherData.setIntensity(0.9);
        } else {
            // Default
            weatherData.setWeather("cloudy");
            weatherData.setIntensity(0.5);
        }

        return weatherData;
    }

    private double calculateIntensity(OpenWeatherResponse response) {
        // A simple calculation for intensity based on humidity
        // Higher humidity typically means more potential for rain
        double humidity = response.getMain().getHumidity();
        return Math.min(0.3 + (humidity / 100.0), 1.0);
    }
}