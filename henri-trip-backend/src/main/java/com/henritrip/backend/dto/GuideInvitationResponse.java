package com.henritrip.backend.dto;

public record GuideInvitationResponse(
        Long id,
        Long userId,
        String userEmail,
        Long guideId
) {}