
package com.henritrip.backend.controller;

import com.henritrip.backend.dto.GuideActivityRequest;
import com.henritrip.backend.dto.GuideActivityResponse;
import com.henritrip.backend.service.GuideActivityService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/guides/{guideId}/activities")
public class GuideActivityController {

    private final GuideActivityService guideActivityService;

    public GuideActivityController(
            GuideActivityService guideActivityService) {
        this.guideActivityService = guideActivityService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GuideActivityResponse addActivityToGuide(
            @PathVariable Long guideId,
            @Valid @RequestBody GuideActivityRequest request) {

        return guideActivityService.addActivityToGuide(
                guideId,
                request
        );
    }
    
    @PutMapping("/{associationId}")
    public GuideActivityResponse updateGuideActivity(
            @PathVariable Long guideId,
            @PathVariable Long associationId,
            @Valid @RequestBody GuideActivityRequest request) {

        return guideActivityService.updateGuideActivity(
                guideId,
                associationId,
                request
        );
    }

    @DeleteMapping("/{associationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeActivityFromGuide(
            @PathVariable Long guideId,
            @PathVariable Long associationId) {

        guideActivityService.removeActivityFromGuide(
                guideId,
                associationId
        );
    }
}
