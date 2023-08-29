package com.khamenev.dryke.client;

import com.khamenev.dryke.model.OpenMeteoResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
@Component
public interface OpenMeteoAPI {

 Mono<OpenMeteoResponse> getForecast(double latitude, double longitude);

}

