package com.henritrip.backend.repository;

import com.henritrip.backend.entity.GuideActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GuideActivityRepository
        extends JpaRepository<GuideActivity, Long> {

    List<GuideActivity> findByGuideIdOrderByDayNumberAscVisitOrderAsc(
            Long guideId
    );
    
    boolean existsByActivityId(Long activityId);
}