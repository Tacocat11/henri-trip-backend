package com.henritrip.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.henritrip.backend.dto.GuideRequest;
import com.henritrip.backend.dto.GuideResponse;
import com.henritrip.backend.service.GuideService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/guides")
public class GuideController {

    private final GuideService guideService;

    public GuideController(GuideService guideService) {
        this.guideService = guideService;
    }

    @GetMapping
    public List<GuideResponse> getAllGuides() {
        return guideService.getAllGuides();
    }

    @GetMapping("/{id}")
    public GuideResponse getGuideById(@PathVariable Long id) {
        return guideService.getGuideById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GuideResponse createGuide(
            @Valid @RequestBody GuideRequest request) {
        return guideService.createGuide(request);
    }

    @PutMapping("/{id}")
    public GuideResponse updateGuide(
            @PathVariable Long id,
            @Valid @RequestBody GuideRequest request) {
        return guideService.updateGuide(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGuide(@PathVariable Long id) {
        guideService.deleteGuide(id);
    }
}