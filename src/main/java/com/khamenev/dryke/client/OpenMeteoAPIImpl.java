package com.khamenev.dryke.client;

import com.khamenev.dryke.model.OpenMeteoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class OpenMeteoAPIImpl implements OpenMeteoAPI {
    private final WebClient webClient;

    @Autowired
    public OpenMeteoAPIImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.open-meteo.com/v1/forecast").build();
    }

    @Override
    public Mono<OpenMeteoResponse> getForecast(double latitude, double longitude) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("")
                        .queryParam("latitude", latitude)
                        .queryParam("longitude", longitude)
                        .queryParam("hourly", "temperature_2m,relativehumidity_2m,precipitation_probability,precipitation,rain,showers,snowfall,weathercode,cloudcover,windspeed_10m,windgusts_10m,is_day")
                        .queryParam("current_weather", "true")
                        .queryParam("forecast_days", "3")
                        .build())
                .retrieve()
                .bodyToMono(OpenMeteoResponse.class);
    }
}

