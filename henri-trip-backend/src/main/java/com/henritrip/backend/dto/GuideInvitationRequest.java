package com.henritrip.backend.dto;

import jakarta.validation.constraints.NotNull;

public record GuideInvitationRequest(
        @NotNull Long userId
) {}