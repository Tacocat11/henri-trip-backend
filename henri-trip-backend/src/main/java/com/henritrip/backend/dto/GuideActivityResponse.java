package com.henritrip.backend.dto;

public record GuideActivityResponse(

    Long id,
    Long activityId,
    String title,
    Integer dayNumber,
    Integer visitOrder

) {
}