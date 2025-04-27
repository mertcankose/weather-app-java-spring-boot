# Weather API Application

This Spring Boot application fetches weather data from OpenWeatherMap API for Turkish cities and provides it through a REST API.

## Features

- Fetches real-time weather data for 11 major Turkish cities
- Maps OpenWeatherMap data to a simplified weather data model
- Includes fallback mechanism for when the external API is unavailable
- Provides REST API endpoints to access the weather data

## Prerequisites

- Java 11 or higher
- Maven
- OpenWeatherMap API key (get it from [OpenWeatherMap](https://openweathermap.org/api))

## Setup

1. Clone the repository
2. Open `src/main/resources/application.properties`
3. Replace `YOUR_API_KEY` with your actual OpenWeatherMap API key:
   ```
   openweather.api.key=YOUR_API_KEY
   ```
4. Build the application using Maven:
   ```
   mvn clean install
   ```
5. Run the application:
   ```
   mvn spring-boot:run
   ```

## API Endpoints

### Get Weather Data for Turkish Cities

```
GET /api/weather
```

**Response Format:**
```json
[
  {
    "lon": 28.9784,
    "lat": 41.0082,
    "name": "İstanbul",
    "intensity": 0.8,
    "temperature": 18,
    "weather": "rainy"
  },
  {
    "lon": 32.8597,
    "lat": 39.9334,
    "name": "Ankara",
    "intensity": 0.2,
    "temperature": 15,
    "weather": "cloudy"
  },
  ...
]
```

**Weather Types:**
- `sunny`: Clear sky conditions
- `cloudy`: Cloudy conditions
- `rainy`: Rain conditions
- `snowy`: Snow conditions
- `stormy`: Thunderstorm conditions

**Intensity:**
A value between 0 and 1 representing the intensity of the weather condition.

## Error Handling

The API includes comprehensive error handling:

- If OpenWeatherMap API is unavailable, the application will use fallback data
- All errors are properly logged and appropriate HTTP status codes are returned

## Data Model

The weather data model includes:
- `lon`: Longitude of the city
- `lat`: Latitude of the city
- `name`: Name of the city
- `intensity`: Weather intensity (0-1)
- `temperature`: Temperature in Celsius
- `weather`: Weather condition (sunny, cloudy, rainy, snowy, stormy)

## Cities Included

The application fetches weather data for the following Turkish cities:
- Istanbul
- Ankara
- Izmir
- Denizli
- Adana
- Erzurum
- Antalya
- Kayseri
- Bursa
- Gaziantep
- Trabzon