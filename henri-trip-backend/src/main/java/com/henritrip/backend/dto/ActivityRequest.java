package com.henritrip.backend.dto;

import com.henritrip.backend.entity.ActivityCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ActivityRequest(

    @NotBlank
    String title,

    String description,

    @NotNull
    ActivityCategory category,

    String address,
    String phone,
    String openingHours,
    String website

) {
}