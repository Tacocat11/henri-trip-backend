package com.henritrip.backend.dto;

import com.henritrip.backend.entity.Audience;
import com.henritrip.backend.entity.Mobility;
import com.henritrip.backend.entity.Season;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record GuideRequest(

    @NotBlank
    String title,

    String description,

    @NotNull
    @Min(1)
    Integer numberOfDays,

    Set<Mobility> mobilities,

    Set<Season> seasons,

    Set<Audience> audiences

) {
}