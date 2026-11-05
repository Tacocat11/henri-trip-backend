
package com.henritrip.backend.service;

import com.henritrip.backend.dto.GuideActivityRequest;
import com.henritrip.backend.dto.GuideActivityResponse;
import com.henritrip.backend.entity.Activity;
import com.henritrip.backend.entity.Guide;
import com.henritrip.backend.entity.GuideActivity;
import com.henritrip.backend.repository.ActivityRepository;
import com.henritrip.backend.repository.GuideRepository;
import com.henritrip.backend.repository.GuideActivityRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class GuideActivityService {

    private final GuideActivityRepository guideActivityRepository;
    private final GuideRepository guideRepository;
    private final ActivityRepository activityRepository;

    public GuideActivityService(
            GuideActivityRepository guideActivityRepository,
            GuideRepository guideRepository,
            ActivityRepository activityRepository) {

        this.guideActivityRepository = guideActivityRepository;
        this.guideRepository = guideRepository;
        this.activityRepository = activityRepository;
    }

    public GuideActivityResponse addActivityToGuide(
            Long guideId,
            GuideActivityRequest request) {

        // 1. Vérifier que le guide existe
        Guide guide = guideRepository.findById(guideId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Guide introuvable : " + guideId
                ));

        // 2. Vérifier que l'activité existe
        Activity activity = activityRepository.findById(request.activityId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Activité introuvable : " + request.activityId()
                ));

        // 3. Vérifier que le jour appartient au guide
        if (request.dayNumber() > guide.getNumberOfDays()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le jour demandé dépasse la durée du guide."
            );
        }

        // 4. Vérifier que l'emplacement est libre
        if (guideActivityRepository
                .existsByGuideIdAndDayNumberAndVisitOrder(
                        guideId,
                        request.dayNumber(),
                        request.visitOrder())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Une activité occupe déjà cet emplacement."
            );
        }

        // 5. Créer l'association
        GuideActivity guideActivity = new GuideActivity();

        guideActivity.setGuide(guide);
        guideActivity.setActivity(activity);
        guideActivity.setDayNumber(request.dayNumber());
        guideActivity.setVisitOrder(request.visitOrder());

        GuideActivity saved = guideActivityRepository.save(guideActivity);

        // 6. Retourner le DTO
        return new GuideActivityResponse(
                saved.getId(),
                activity.getId(),
                activity.getTitle(),
                saved.getDayNumber(),
                saved.getVisitOrder()
        );
    }
    
    public GuideActivityResponse updateGuideActivity(
            Long guideId,
            Long associationId,
            GuideActivityRequest request) {

        GuideActivity guideActivity = findGuideActivity(
                guideId, associationId
        );

        Guide guide = guideActivity.getGuide();

        Activity activity = activityRepository
                .findById(request.activityId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Activité introuvable : " + request.activityId()
                ));

        // Vérifier que le jour est valide
        if (request.dayNumber() > guide.getNumberOfDays()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Le jour demandé dépasse la durée du guide."
            );
        }

        // Vérifier si la position est déjà occupée
        boolean positionChanged =
                !guideActivity.getDayNumber().equals(request.dayNumber())
                || !guideActivity.getVisitOrder().equals(request.visitOrder());

        if (positionChanged
                && guideActivityRepository
                        .existsByGuideIdAndDayNumberAndVisitOrder(
                                guideId,
                                request.dayNumber(),
                                request.visitOrder())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Une activité occupe déjà cet emplacement."
            );
        }

        // Mettre à jour l'association
        guideActivity.setActivity(activity);
        guideActivity.setDayNumber(request.dayNumber());
        guideActivity.setVisitOrder(request.visitOrder());

        GuideActivity saved = guideActivityRepository.save(guideActivity);

        return new GuideActivityResponse(
                saved.getId(),
                activity.getId(),
                activity.getTitle(),
                saved.getDayNumber(),
                saved.getVisitOrder()
        );
    }
    
    public void removeActivityFromGuide(
            Long guideId,
            Long associationId) {

        GuideActivity guideActivity = findGuideActivity(
                guideId, associationId
        );

        guideActivityRepository.delete(guideActivity);
    }
    
    private GuideActivity findGuideActivity(Long guideId, Long associationId) {

        GuideActivity guideActivity = guideActivityRepository
                .findById(associationId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Association introuvable : " + associationId
                ));

        if (!guideActivity.getGuide().getId().equals(guideId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Cette association n'appartient pas au guide : " + guideId
            );
        }

        return guideActivity;
    }
}
