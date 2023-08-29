package com.khamenev.dryke.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class OpenMeteoResponse {

    private Double latitude;
    private Double longitude;

    @JsonProperty("generation_time_ms")
    private Double generationTimeMs;

    @JsonProperty("utc_offset_seconds")
    private Integer utcOffsetSeconds;

    private String timezone;

    @JsonProperty("timezone_abbreviation")
    private String timezoneAbbreviation;

    private Double elevation;

    @JsonProperty("current_weather")
    private CurrentWeather currentWeather;

    @JsonProperty("hourly_units")
    private HourlyUnits hourlyUnits;

    private Hourly hourly;

    @Data
    public static class CurrentWeather {
        private Double temperature;

        @JsonProperty("windspeed")
        private Double windSpeed;

        @JsonProperty("winddirection")
        private Integer windDirection;

        @JsonProperty("weathercode")
        private Integer weatherCode;

        @JsonProperty("is_day")
        private Integer isDay;

        private String time;
    }

    @Data
    public static class HourlyUnits {

        private String time;

        @JsonProperty("temperature_2m")
        private String temperature2m;

        @JsonProperty("relative_humidity_2m")
        private String relativeHumidity2m;

        @JsonProperty("precipitation_probability")
        private String precipitationProbability;

        private String precipitation;
        private String rain;
        private String showers;
        private String snowfall;

        @JsonProperty("weather_code")
        private String weatherCode;

        @JsonProperty("cloud_cover")
        private String cloudCover;

        @JsonProperty("wind_speed_10m")
        private String windSpeed10m;

        @JsonProperty("wind_gusts_10m")
        private String windGusts10m;

        @JsonProperty("is_day")
        private String isDay;
    }

    @Data
    public static class Hourly {

        private List<String> time;

        @JsonProperty("temperature_2m")
        private List<Double> temperature2m;

        @JsonProperty("relativehumidity_2m")
        private List<Integer> relativeHumidity2m;

        @JsonProperty("precipitation_probability")
        private List<Integer> precipitationProbability;

        private List<Double> precipitation;
        private List<Double> rain;
        private List<Double> showers;
        private List<Double> snowfall;

        @JsonProperty("weathercode")
        private List<Integer> weatherCode;

        @JsonProperty("cloudcover")
        private List<Integer> cloudCover;

        @JsonProperty("windspeed_10m")
        private List<Double> windSpeed10m;

        @JsonProperty("windgusts_10m")
        private List<Double> windGusts10m;

        @JsonProperty("is_day")
        private List<Integer> isDay;
    }
}
