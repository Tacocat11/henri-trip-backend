
package com.henritrip.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record GuideActivityRequest(

    @NotNull
    Long activityId,

    @NotNull
    @Min(1)
    Integer dayNumber,

    @NotNull
    @Min(1)
    Integer visitOrder

) {}
