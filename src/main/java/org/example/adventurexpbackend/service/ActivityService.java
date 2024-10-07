package org.example.adventurexpbackend.service;

import org.example.adventurexpbackend.model.Activity;
import org.example.adventurexpbackend.model.Equipment;
import org.example.adventurexpbackend.model.EquipmentType;
import org.example.adventurexpbackend.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;

    @Autowired
    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Transactional
    public Activity saveActivity(Activity activity) {
        activity.getEquipmentList().forEach(equipment -> equipment.setActivity(activity));
        activity.getEquipmentTypes().forEach(equipmentType -> equipmentType.setActivity(activity));
        return activityRepository.save(activity);
    }

    public List<Activity> getAllActivities() {
        return activityRepository.findAll();
    }

    @Transactional
    public List<Activity> saveAllActivities(List<Activity> activities) {
        List<Activity> savedActivities = new ArrayList<>();
        for (Activity activity : activities) {
            savedActivities.add(saveActivity(activity)); // Transactional
        }
        return savedActivities;
    }

    public Activity getActivity(Activity activity) {
        if (activity.getId() != null) {
            return activityRepository.findById(activity.getId()).orElse(null);
        } else {
            return activityRepository.findByName(activity.getName());
        }
    }
}