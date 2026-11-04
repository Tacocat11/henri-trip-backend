package com.henritrip.backend.service;

import com.henritrip.backend.dto.ActivityRequest;
import com.henritrip.backend.dto.ActivityResponse;
import com.henritrip.backend.entity.Activity;
import com.henritrip.backend.repository.ActivityRepository;
import com.henritrip.backend.repository.GuideActivityRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final GuideActivityRepository guideActivityRepository;

    public ActivityService(
            ActivityRepository activityRepository,
            GuideActivityRepository guideActivityRepository) {

        this.activityRepository = activityRepository;
        this.guideActivityRepository = guideActivityRepository;
    }

    @Transactional(readOnly = true)
    public List<ActivityResponse> getAllActivities() {
    	 return activityRepository.findAll()
                 .stream()
                 .map(this::toActivityResponse)
                 .toList();
    }

    @Transactional(readOnly = true)
    public ActivityResponse getActivityById(Long id) {
        Activity activity = findActivity(id);
        return toActivityResponse(activity);
    }

    private Activity findActivity(Long id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Activité introuvable : " + id
                ));
    }

    public ActivityResponse createActivity(ActivityRequest request) {
        Activity activity = new Activity();

        updateFields(activity, request);

        return toActivityResponse(activityRepository.save(activity));
    }

    public ActivityResponse updateActivity(Long id, ActivityRequest request) {
        Activity activity = findActivity(id);

        updateFields(activity, request);

        return toActivityResponse(activityRepository.save(activity));
    }

    private void updateFields(Activity activity, ActivityRequest request) {
        activity.setTitle(request.title());
        activity.setDescription(request.description());
        activity.setCategory(request.category());
        activity.setAddress(request.address());
        activity.setPhone(request.phone());
        activity.setOpeningHours(request.openingHours());
        activity.setWebsite(request.website());
    }

    public void deleteActivity(Long id) {
        Activity activity = findActivity(id);

        if (guideActivityRepository.existsByActivityId(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Impossible de supprimer cette activité : elle est utilisée dans un guide."
            );
        }
        activityRepository.delete(activity);
    }
    
    private ActivityResponse toActivityResponse(Activity activity) {
        return new ActivityResponse(
                activity.getId(),
                activity.getTitle(),
                activity.getDescription(),
                activity.getCategory(),
                activity.getAddress(),
                activity.getPhone(),
                activity.getOpeningHours(),
                activity.getWebsite()
        );
    }
}
