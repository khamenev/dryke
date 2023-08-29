package com.khamenev.dryke.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckResult {
    private Boolean isRecommended;
    private Integer hours;
    private String message;
}
