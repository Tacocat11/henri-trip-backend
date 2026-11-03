package com.henritrip.backend.dto;

import com.henritrip.backend.entity.ActivityCategory;

public record ActivityResponse(

    Long id,
    String title,
    String description,
    ActivityCategory category,
    String address,
    String phone,
    String openingHours,
    String website

) {
}