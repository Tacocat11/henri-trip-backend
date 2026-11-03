package com.henritrip.backend.dto;

import com.henritrip.backend.entity.Role;

public record UserResponse(
    Long id,
    String email,
    Role role
) {
}