
package com.henritrip.backend.service;

import com.henritrip.backend.dto.LoginRequest;
import com.henritrip.backend.dto.LoginResponse;
import com.henritrip.backend.entity.User;
import com.henritrip.backend.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthService(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password()
            )
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() ->
                    new IllegalArgumentException(
                        "Utilisateur introuvable."
                    )
                );

        String token = jwtService.generateToken(
            user.getEmail(),
            user.getRole().name()
        );

        return new LoginResponse(
            token,
            user.getId(),
            user.getEmail(),
            user.getRole()
        );
    }
}
