
package com.henritrip.backend.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.henritrip.backend.dto.GuideResponse;
import com.henritrip.backend.service.MyGuideService;

@RestController
@RequestMapping("/api/my/guides")
public class MyGuideController {

    private final MyGuideService myGuideService;

    public MyGuideController(MyGuideService myGuideService) {
        this.myGuideService = myGuideService;
    }

    @GetMapping
    public List<GuideResponse> getMyGuides(
            Authentication authentication) {

        return myGuideService.getMyGuides(
                authentication.getName()
        );
    }

    @GetMapping("/{id}")
    public GuideResponse getMyGuide(
            @PathVariable Long id,
            Authentication authentication) {

        return myGuideService.getMyGuide(
                authentication.getName(),
                id
        );
    }
}
