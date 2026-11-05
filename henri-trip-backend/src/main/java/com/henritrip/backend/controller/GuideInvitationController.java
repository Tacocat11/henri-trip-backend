package com.henritrip.backend.controller;

import com.henritrip.backend.dto.GuideInvitationRequest;
import com.henritrip.backend.dto.GuideInvitationResponse;
import com.henritrip.backend.service.GuideInvitationService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guides/{guideId}/invitations")
public class GuideInvitationController {

    private final GuideInvitationService invitationService;

    public GuideInvitationController(
            GuideInvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @GetMapping
    public List<GuideInvitationResponse> getInvitations(
            @PathVariable Long guideId) {

        return invitationService.getInvitationsForGuide(guideId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GuideInvitationResponse inviteUser(
            @PathVariable Long guideId,
            @Valid @RequestBody GuideInvitationRequest request) {

        return invitationService.inviteUser(
                request.userId(),
                guideId
        );
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void revokeInvitation(
            @PathVariable Long guideId,
            @PathVariable Long userId) {

        invitationService.revokeInvitation(userId, guideId);
    }
}
