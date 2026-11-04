package com.henritrip.backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.henritrip.backend.dto.UserRequest;
import com.henritrip.backend.dto.UserResponse;
import com.henritrip.backend.entity.User;
import com.henritrip.backend.repository.UserRepository;
import com.henritrip.backend.repository.GuideInvitationRepository;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final GuideInvitationRepository invitationRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            GuideInvitationRepository invitationRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.invitationRepository = invitationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::toUserResponse)
                .toList();
    }

    public UserResponse createUser(UserRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cette adresse email est déjà utilisée."
            );
        }

        User user = new User();

        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(request.role());

        User savedUser = userRepository.save(user);

        return toUserResponse(savedUser);
    }

    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Utilisateur introuvable."
                ));

        // Suppression des invitations associées à l'utilisateur
        invitationRepository.deleteAll(
                invitationRepository.findByUserId(id)
        );

        userRepository.delete(user);
    }

    private UserResponse toUserResponse(User user) {

        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getRole()
        );
    }
}