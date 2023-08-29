package com.khamenev.dryke;

import com.khamenev.dryke.client.OpenMeteoAPI;
import com.khamenev.dryke.model.*;
import com.khamenev.dryke.service.ForecastService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ForecastServiceTest {

    @InjectMocks
    private ForecastService forecastService;

    @Mock
    private OpenMeteoAPI openMeteoAPI;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDryOrNot() {
        // Setup test data
        Location location = new Location(40.7128, -74.0060);
        OpenMeteoResponse mockResponse = new OpenMeteoResponse();
        // Populate mockResponse with appropriate data

        // Setup mock behavior
        when(openMeteoAPI.getForecast(anyDouble(), anyDouble())).thenReturn(Mono.just(mockResponse));

        // Execute method
        ApiResponse<Recommendation> apiResponse = forecastService.dryOrNot(location);

        // Verify and Assert
        verify(openMeteoAPI, times(1)).getForecast(anyDouble(), anyDouble());
        assertNotNull(apiResponse);
        // Further assertions based on what you expect
    }

    @Test
    public void testSliceFromIndex() {
        List<Integer> originalList = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> expectedList = Arrays.asList(3, 4, 5);
        List<Integer> actualList = ForecastService.sliceFromIndex(originalList, 2);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testCheckTemperatureForecast() {
        List<Double> temperatureForecast = Arrays.asList(16.0, 17.0, 18.0);
        CheckResult result = ForecastService.checkTemperatureForecast(temperatureForecast);
        assertTrue(result.getIsRecommended());
        assertEquals(12, result.getHours());
        assertNull(result.getMessage());
    }

    @Test
    public void testCheckWindForecast() {
        List<Double> windForecast = Arrays.asList(10.0, 12.0, 14.0);
        CheckResult result = ForecastService.checkWindForecast(windForecast);
        assertTrue(result.getIsRecommended());
        assertEquals(12, result.getHours());
        assertNull(result.getMessage());
    }

    @Test
    public void testCheckHumidity() {
        List<Integer> humidityForecast = Arrays.asList(50, 55, 60);
        CheckResult result = ForecastService.checkHumidity(humidityForecast);
        assertTrue(result.getIsRecommended());
        assertEquals(12, result.getHours());
        assertNull(result.getMessage());
    }

    @Test
    public void testCheckPrecipitationProbabilityForecast() {
        List<Integer> precipitationProbabilityForecast = Arrays.asList(10, 15, 20);
        CheckResult result = ForecastService.checkPrecipitationProbabilityForecast(precipitationProbabilityForecast);
        assertTrue(result.getIsRecommended());
        assertEquals(12, result.getHours());
        assertNull(result.getMessage());
    }

    @Test
    public void testCalculateRecommendation() {
        List<CheckResult> checkResults = Arrays.asList(
                new CheckResult(true, 12, null),
                new CheckResult(false, 12, "Low temperature")
        );
        Recommendation recommendation = ForecastService.calculateRecommendation(checkResults);
        assertFalse(recommendation.getIsRecommended());
        assertEquals(1, recommendation.getRejectReasons().size());
        assertEquals("Low temperature", recommendation.getRejectReasons().get(0));
    }
}
