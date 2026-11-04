
package com.henritrip.backend.dto;

import com.henritrip.backend.entity.Role;

public record LoginResponse(
    String token,
    Long userId,
    String email,
    Role role
) {
}
