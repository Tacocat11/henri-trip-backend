
package com.henritrip.backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.henritrip.backend.dto.GuideResponse;
import com.henritrip.backend.entity.User;
import com.henritrip.backend.repository.GuideInvitationRepository;
import com.henritrip.backend.repository.UserRepository;

@Service
@Transactional(readOnly = true)
public class MyGuideService {

    private final UserRepository userRepository;
    private final GuideInvitationRepository invitationRepository;
    private final GuideService guideService;

    public MyGuideService(
            UserRepository userRepository,
            GuideInvitationRepository invitationRepository,
            GuideService guideService) {

        this.userRepository = userRepository;
        this.invitationRepository = invitationRepository;
        this.guideService = guideService;
    }

    public List<GuideResponse> getMyGuides(String email) {

        User user = findUser(email);

        return invitationRepository.findByUserId(user.getId())
                .stream()
                .map(invitation ->
                        guideService.getGuideById(
                                invitation.getGuide().getId()
                        ))
                .toList();
    }

    public GuideResponse getMyGuide(String email, Long guideId) {

        User user = findUser(email);

        if (!invitationRepository.existsByUserIdAndGuideId(
                user.getId(), guideId)) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Guide introuvable ou accès non autorisé."
            );
        }

        return guideService.getGuideById(guideId);
    }

    private User findUser(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Utilisateur introuvable."
                ));
    }
}
