package com.khamenev.dryke.controller;

import com.khamenev.dryke.model.ApiResponse;
import com.khamenev.dryke.model.Location;
import com.khamenev.dryke.service.ForecastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/forecast")
public class ForecastController {
    @Autowired
    private final ForecastService forecastService;

    public ForecastController(ForecastService forecastService) {
        this.forecastService = forecastService;
    }

    @PostMapping("/dryOrNot")
    public ResponseEntity<ApiResponse> dryOrNot(@RequestBody Location location) {
        ApiResponse apiResponse = forecastService.dryOrNot(location);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
