
package com.henritrip.backend.service;

import com.henritrip.backend.dto.GuideInvitationResponse;
import com.henritrip.backend.entity.Guide;
import com.henritrip.backend.entity.GuideInvitation;
import com.henritrip.backend.entity.User;
import com.henritrip.backend.repository.GuideInvitationRepository;
import com.henritrip.backend.repository.GuideRepository;
import com.henritrip.backend.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class GuideInvitationService {

    private final GuideInvitationRepository invitationRepository;
    private final UserRepository userRepository;
    private final GuideRepository guideRepository;

    public GuideInvitationService(
            GuideInvitationRepository invitationRepository,
            UserRepository userRepository,
            GuideRepository guideRepository) {
        this.invitationRepository = invitationRepository;
        this.userRepository = userRepository;
        this.guideRepository = guideRepository;
    }

    @Transactional(readOnly = true)
    public List<GuideInvitationResponse> getInvitationsForGuide(Long guideId) {

        if (!guideRepository.existsById(guideId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Guide introuvable : " + guideId
            );
        }

        return invitationRepository.findByGuideId(guideId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public GuideInvitationResponse inviteUser(Long userId, Long guideId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Utilisateur introuvable : " + userId
                ));

        Guide guide = guideRepository.findById(guideId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Guide introuvable : " + guideId
                ));

        if (invitationRepository.existsByUserIdAndGuideId(userId, guideId)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cet utilisateur est déjà invité à ce guide."
            );
        }

        GuideInvitation invitation = new GuideInvitation();
        invitation.setUser(user);
        invitation.setGuide(guide);

        return toResponse(invitationRepository.save(invitation));
    }

    public void revokeInvitation(Long userId, Long guideId) {

        if (!invitationRepository.existsByUserIdAndGuideId(userId, guideId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Cette invitation n'existe pas."
            );
        }

        invitationRepository.deleteByUserIdAndGuideId(userId, guideId);
    }
    
    private GuideInvitationResponse toResponse(GuideInvitation invitation) {
        return new GuideInvitationResponse(
                invitation.getId(),
                invitation.getUser().getId(),
                invitation.getUser().getEmail(),
                invitation.getGuide().getId()
        );
    }
}
