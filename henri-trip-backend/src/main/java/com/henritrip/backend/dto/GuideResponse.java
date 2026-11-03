package com.henritrip.backend.dto;

import com.henritrip.backend.entity.Audience;
import com.henritrip.backend.entity.Mobility;
import com.henritrip.backend.entity.Season;

import java.util.List;
import java.util.Set;

public record GuideResponse(

    Long id,
    String title,
    String description,
    Integer numberOfDays,
    Set<Mobility> mobilities,
    Set<Season> seasons,
    Set<Audience> audiences,
    List<GuideActivityResponse> activities

) {
}