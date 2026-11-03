
package com.henritrip.backend.service;

import com.henritrip.backend.entity.Guide;
import com.henritrip.backend.entity.GuideInvitation;
import com.henritrip.backend.entity.User;
import com.henritrip.backend.repository.GuideInvitationRepository;
import com.henritrip.backend.repository.GuideRepository;
import com.henritrip.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<GuideInvitation> getInvitationsForGuide(Long guideId) {
        return invitationRepository.findByGuideId(guideId);
    }

    public GuideInvitation inviteUser(Long userId, Long guideId) {
        if (invitationRepository.existsByUserIdAndGuideId(userId, guideId)) {
            throw new IllegalArgumentException(
                    "Cet utilisateur est déjà invité à ce guide.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Utilisateur introuvable : " + userId));

        Guide guide = guideRepository.findById(guideId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Guide introuvable : " + guideId));

        GuideInvitation invitation = new GuideInvitation();
        invitation.setUser(user);
        invitation.setGuide(guide);

        return invitationRepository.save(invitation);
    }

    public void revokeInvitation(Long userId, Long guideId) {
        if (!invitationRepository.existsByUserIdAndGuideId(userId, guideId)) {
            throw new IllegalArgumentException(
                    "Cette invitation n'existe pas.");
        }

        invitationRepository.deleteByUserIdAndGuideId(userId, guideId);
    }
}
