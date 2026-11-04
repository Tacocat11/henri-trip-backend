
package com.henritrip.backend.controller;

import com.henritrip.backend.dto.ActivityRequest;
import com.henritrip.backend.dto.ActivityResponse;
import com.henritrip.backend.service.ActivityService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping
    public List<ActivityResponse> getAllActivities() {
        return activityService.getAllActivities();
    }

    @GetMapping("/{id}")
    public ActivityResponse getActivityById(@PathVariable Long id) {
        return activityService.getActivityById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ActivityResponse createActivity(
            @Valid @RequestBody ActivityRequest request) {
        return activityService.createActivity(request);
    }

    @PutMapping("/{id}")
    public ActivityResponse updateActivity(
            @PathVariable Long id,
            @Valid @RequestBody ActivityRequest request) {
        return activityService.updateActivity(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteActivity(@PathVariable Long id) {
        activityService.deleteActivity(id);
    }
}

