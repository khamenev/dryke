package com.khamenev.dryke.model;

import java.util.List;

public class OpenMeteoResponse {

    private double latitude;
    private double longitude;
    private double generationtime_ms;
    private int utc_offset_seconds;
    private String timezone;
    private String timezone_abbreviation;
    private double elevation;

    private CurrentWeather current_weather;
    private HourlyUnits hourly_units;
    private Hourly hourly;

    // Standard getters and setters

    public static class CurrentWeather {
        private double temperature;
        private double windspeed;
        private int winddirection;
        private int weathercode;
        private int is_day;
        private String time;

        // Standard getters and setters
    }

    public static class HourlyUnits {
        private String time;
        private String temperature_2m;
        private String relativehumidity_2m;
        private String precipitation_probability;
        private String precipitation;
        private String rain;
        private String showers;
        private String snowfall;
        private String weathercode;
        private String cloudcover;
        private String windspeed_10m;
        private String windgusts_10m;
        private String is_day;

        // Standard getters and setters
    }

    public static class Hourly {
        private List<String> time;
        private List<Double> temperature_2m;
        private List<Integer> relativehumidity_2m;
        private List<Integer> precipitation_probability;
        private List<Double> precipitation;
        private List<Double> rain;
        private List<Double> showers;
        private List<Double> snowfall;
        private List<Integer> weathercode;
        private List<Integer> cloudcover;
        private List<Double> windspeed_10m;
        private List<Double> windgusts_10m;
        private List<Integer> is_day;

        // Standard getters and setters
    }
}

