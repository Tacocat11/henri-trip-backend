package com.henritrip.backend.service;

import com.henritrip.backend.entity.Activity;
import com.henritrip.backend.repository.ActivityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ActivityService {

    private final ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Transactional(readOnly = true)
    public List<Activity> getAllActivities() {
        return activityRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Activity getActivityById(Long id) {
        return activityRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Activité introuvable : " + id));
    }

    public Activity createActivity(Activity activity) {
        activity.setId(null);
        return activityRepository.save(activity);
    }

    public Activity updateActivity(Long id, Activity updatedActivity) {
        Activity activity = getActivityById(id);

        activity.setTitle(updatedActivity.getTitle());
        activity.setDescription(updatedActivity.getDescription());
        activity.setCategory(updatedActivity.getCategory());
        activity.setAddress(updatedActivity.getAddress());
        activity.setPhone(updatedActivity.getPhone());
        activity.setOpeningHours(updatedActivity.getOpeningHours());
        activity.setWebsite(updatedActivity.getWebsite());

        return activityRepository.save(activity);
    }

    public void deleteActivity(Long id) {
        Activity activity = getActivityById(id);
        activityRepository.delete(activity);
    }
}
