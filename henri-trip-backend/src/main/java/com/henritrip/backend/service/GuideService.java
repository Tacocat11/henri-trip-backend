package com.henritrip.backend.service;

import com.henritrip.backend.dto.GuideActivityResponse;
import com.henritrip.backend.dto.GuideRequest;
import com.henritrip.backend.dto.GuideResponse;
import com.henritrip.backend.entity.Guide;
import com.henritrip.backend.entity.GuideActivity;
import com.henritrip.backend.entity.GuideInvitation;
import com.henritrip.backend.repository.GuideActivityRepository;
import com.henritrip.backend.repository.GuideInvitationRepository;
import com.henritrip.backend.repository.GuideRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;

@Service
@Transactional
public class GuideService {

    private final GuideRepository guideRepository;
    private final GuideInvitationRepository invitationRepository;
    private final GuideActivityRepository guideActivityRepository;

    public GuideService(
            GuideRepository guideRepository,
            GuideInvitationRepository invitationRepository,
            GuideActivityRepository guideActivityRepository) {
        this.guideRepository = guideRepository;
        this.invitationRepository = invitationRepository;
        this.guideActivityRepository = guideActivityRepository;
    }

    @Transactional(readOnly = true)
    public List<GuideResponse> getAllGuides() {
        return guideRepository.findAll()
        		.stream()
                .map(this::toGuideResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Guide> getGuidesForUser(Long userId) {
        return invitationRepository.findByUserId(userId)
                .stream()
                .map(invitation -> invitation.getGuide())
                .toList();
    }

    @Transactional(readOnly = true)
    public GuideResponse getGuideById(Long id) {
    	Guide guide = guideRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Guide introuvable."
                ));

        return toGuideResponse(guide);
    }

    public GuideResponse createGuide(GuideRequest request) {

        Guide guide = new Guide();

        guide.setTitle(request.title());
        guide.setDescription(request.description());
        guide.setNumberOfDays(request.numberOfDays());
        guide.setMobilities(request.mobilities());
        guide.setSeasons(request.seasons());
        guide.setAudiences(request.audiences());

        Guide savedGuide = guideRepository.save(guide);

        return toGuideResponse(savedGuide);
    }

    public GuideResponse updateGuide(Long id, GuideRequest request) {
        Guide guide = guideRepository.findById(id)
        			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Guide introuvable."));

        guide.setTitle(request.title());
        guide.setDescription(request.description());
        guide.setNumberOfDays(request.numberOfDays());
        guide.setMobilities(request.mobilities());
        guide.setSeasons(request.seasons());
        guide.setAudiences(request.audiences());

        Guide updatedGuide = guideRepository.save(guide);

        return toGuideResponse(updatedGuide);
    }

    public void deleteGuide(Long id) {
        Guide guide = guideRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Guide introuvable."
                ));

        List<GuideActivity> activities = guideActivityRepository.findByGuideIdOrderByDayNumberAscVisitOrderAsc(id);
        guideActivityRepository.deleteAll(activities);

        List<GuideInvitation> invitations = invitationRepository.findByGuideId(id);
        invitationRepository.deleteAll(invitations);

        guideRepository.delete(guide);
    }
    
    private GuideResponse toGuideResponse(Guide guide) {

        List<GuideActivityResponse> activities =
                guideActivityRepository
                        .findByGuideIdOrderByDayNumberAscVisitOrderAsc(guide.getId())
                        .stream()
                        .map(guideActivity -> new GuideActivityResponse(
                                guideActivity.getId(),
                                guideActivity.getActivity().getId(),
                                guideActivity.getActivity().getTitle(),
                                guideActivity.getDayNumber(),
                                guideActivity.getVisitOrder()
                        ))
                        .toList();

        return new GuideResponse(
                guide.getId(),
                guide.getTitle(),
                guide.getDescription(),
                guide.getNumberOfDays(),
                new HashSet<>(guide.getMobilities()),
                new HashSet<>(guide.getSeasons()),
                new HashSet<>(guide.getAudiences()),
                activities
        );
    }
}