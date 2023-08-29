package com.khamenev.dryke.model;

import lombok.Data;

import java.util.List;

@Data
public class Recommendation {
    private Boolean isRecommended;
    private List<String> recommendations;
    private List<String> rejectReasons;
}
