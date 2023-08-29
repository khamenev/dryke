package com.khamenev.dryke.service;

import com.khamenev.dryke.client.OpenMeteoAPI;
import com.khamenev.dryke.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

@Service
public class ForecastService {
    private final OpenMeteoAPI openMeteoAPI;

    @Autowired
    public ForecastService(OpenMeteoAPI openMeteoAPI) {
        this.openMeteoAPI = openMeteoAPI;
    }

    public ApiResponse<Recommendation> dryOrNot(Location location) {
        ApiResponse<Recommendation> response = new ApiResponse<>();
        try {
            OpenMeteoResponse forecast = openMeteoAPI.getForecast(location.getLatitude(), location.getLongitude()).block();

            if (forecast != null && forecast.getCurrentWeather() != null) {
                Recommendation recommendation = calculateRecommendation(forecast);
                response.setData(recommendation);
            } else {
                response.setError(new ErrorInfo("Could not fetch forecast."));
            }
        } catch (Exception e) {
            response.setError(new ErrorInfo("An error occurred while fetching the forecast: " + e.getMessage()));
        }
        return response;
    }

    private Recommendation calculateRecommendation(OpenMeteoResponse forecast) {
        // Get current time
        String currentTime = forecast.getCurrentWeather().getTime();
        int currentElement = forecast.getHourly().getTime().indexOf(currentTime);

        // Slice list of measures since current time
        List<Double> slicedTemperatureList = sliceFromIndex(forecast.getHourly().getTemperature2m(), currentElement);
        List<Double> slicedWindList = sliceFromIndex(forecast.getHourly().getWindGusts10m(), currentElement);
        List<Integer> slicedHumidityList = sliceFromIndex(forecast.getHourly().getRelativeHumidity2m(), currentElement);
        List<Integer> slicedrecipitationProbabilityList = sliceFromIndex(forecast.getHourly().getPrecipitationProbability(), currentElement);

        // Checks
        List<CheckResult> checkResults = new ArrayList<>();

        // Temperature in 12 hours
        CheckResult temperatureForecastCheck = checkTemperatureForecast(slicedTemperatureList);
        if (!temperatureForecastCheck.getIsRecommended() || temperatureForecastCheck.getHours() == 6) {
            checkResults.add(temperatureForecastCheck);
        }

        // Wind 10 m in 12 hours
        CheckResult windForecastCheck = checkWindForecast(slicedWindList);
        if (!windForecastCheck.getIsRecommended() || windForecastCheck.getHours() == 6) {
            checkResults.add(windForecastCheck);
        }

        // Relative humidity 2m in 12 hours
        CheckResult humidityCheckCheck = checkHumidity(slicedHumidityList);
        if (!humidityCheckCheck.getIsRecommended() || humidityCheckCheck.getHours() == 6) {
            checkResults.add(humidityCheckCheck);
        }

        // Precipitation probability in 12 hours
        CheckResult rainForecastCheck = checkPrecipitationProbabilityForecast(slicedrecipitationProbabilityList);
        if (!rainForecastCheck.getIsRecommended() || rainForecastCheck.getHours() == 6) {
            checkResults.add(rainForecastCheck);
        }

        return calculateRecommendation(checkResults);
    }

    public static <T> List<T> sliceFromIndex(List<T> originalList, int startIndex) {
        return originalList.stream()
                .skip(startIndex)
                .collect(Collectors.toList());
    }

    public static CheckResult checkTemperatureForecast(List<Double> temperatureForecast) {
        OptionalDouble avgTemperature = temperatureForecast.stream().limit(12).mapToDouble(a -> a).average();
        CheckResult checkResult = new CheckResult(true, 12, null);
        if (avgTemperature.orElse(0.0) < 15.0) {
            checkResult.setIsRecommended(false);
            checkResult.setMessage("Low temperature");
            return checkResult;
        }
        return checkResult;
    }

    public static CheckResult checkWindForecast(List<Double> windForecast) {
        CheckResult checkResult = new CheckResult(true, 12, null);
        if (windForecast.stream().limit(6).anyMatch(speed -> speed > 20.0)) {
            checkResult.setIsRecommended(false);
            checkResult.setMessage("Wind");
            return checkResult;
        } else if (windForecast.stream().skip(6).anyMatch(speed -> speed > 20.0)) {
            checkResult.setIsRecommended(true);
            checkResult.setHours(6);
            checkResult.setMessage("Remove your items after 6 hours because of possible wind.");
            return checkResult;
        }

        return checkResult;
    }

    public static CheckResult checkHumidity(List<Integer> humidityForecast) {
        CheckResult checkResult = new CheckResult(true, 12, null);
        OptionalDouble avgHumidity = humidityForecast.stream().limit(12).mapToDouble(a -> a).average();
        if (avgHumidity.orElse(0.0) > 80.0) {
            checkResult.setIsRecommended(false);
            checkResult.setMessage("High humidity");
            return checkResult;
        }
        return checkResult;
    }

    public static CheckResult checkPrecipitationProbabilityForecast(List<Integer> precipitationProbabilityForecast) {
        CheckResult checkResult = new CheckResult(true, 12, null);
        if (precipitationProbabilityForecast.stream().limit(6).anyMatch(rain -> rain > 30.0)) {
            checkResult.setIsRecommended(false);
            checkResult.setMessage("Possible rain");
            return checkResult;
        } else if (precipitationProbabilityForecast.stream().skip(6).anyMatch(rain -> rain > 30.0)) {
            checkResult.setIsRecommended(true);
            checkResult.setHours(6);
            checkResult.setMessage("Remove items after 6 hourhs because of possible rain");
            return checkResult;
        }
        return checkResult;
    }

    public static Recommendation calculateRecommendation(List<CheckResult> checkResults) {
        boolean isRecommended = true;
        List<String> recommendations = new ArrayList<>();
        List<String> rejectReasons = new ArrayList<>();

        for (CheckResult checkResult: checkResults) {
            if (!checkResult.getIsRecommended()) {
                isRecommended = false;
                rejectReasons.add(checkResult.getMessage());
            } else {
                recommendations.add(checkResult.getMessage());
            }
        }

        Recommendation recommendation = new Recommendation();
        if (isRecommended) {
            recommendation.setIsRecommended(true);
            recommendation.setRecommendations(recommendations);
        } else {
            recommendation.setIsRecommended(false);
            recommendation.setRejectReasons(rejectReasons);
        }
        return recommendation;
    }
}

