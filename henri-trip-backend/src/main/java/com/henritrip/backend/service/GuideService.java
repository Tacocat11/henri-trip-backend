package com.henritrip.backend.service;

import com.henritrip.backend.entity.Guide;
import com.henritrip.backend.repository.GuideInvitationRepository;
import com.henritrip.backend.repository.GuideRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GuideService {

    private final GuideRepository guideRepository;
    private final GuideInvitationRepository invitationRepository;

    public GuideService(
            GuideRepository guideRepository,
            GuideInvitationRepository invitationRepository) {
        this.guideRepository = guideRepository;
        this.invitationRepository = invitationRepository;
    }

    @Transactional(readOnly = true)
    public List<Guide> getAllGuides() {
        return guideRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Guide> getGuidesForUser(Long userId) {
        return invitationRepository.findByUserId(userId)
                .stream()
                .map(invitation -> invitation.getGuide())
                .toList();
    }

    @Transactional(readOnly = true)
    public Guide getGuideById(Long id) {
        return guideRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Guide introuvable : " + id));
    }

    public Guide createGuide(Guide guide) {
        guide.setId(null);
        return guideRepository.save(guide);
    }

    public Guide updateGuide(Long id, Guide updatedGuide) {
        Guide guide = getGuideById(id);

        guide.setTitle(updatedGuide.getTitle());
        guide.setDescription(updatedGuide.getDescription());
        guide.setNumberOfDays(updatedGuide.getNumberOfDays());
        guide.setMobilities(updatedGuide.getMobilities());
        guide.setSeasons(updatedGuide.getSeasons());
        guide.setAudiences(updatedGuide.getAudiences());

        return guideRepository.save(guide);
    }

    public void deleteGuide(Long id) {
        Guide guide = getGuideById(id);
        guideRepository.delete(guide);
    }
}