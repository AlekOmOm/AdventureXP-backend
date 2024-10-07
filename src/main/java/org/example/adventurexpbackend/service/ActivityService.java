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
import java.util.Optional;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    // ------------------- Create -------------------
    @Transactional
    public Activity saveActivity(Activity activity) {
        activity.getEquipmentList().forEach(equipment -> equipment.setActivity(activity));
        activity.getEquipmentTypes().forEach(equipmentType -> equipmentType.setActivity(activity));
        return activityRepository.save(activity);
    }


    @Transactional
    public List<Activity> saveAllActivities(List<Activity> activities) {
        List<Activity> savedActivities = new ArrayList<>();
        for (Activity activity : activities) {
            savedActivities.add(saveActivity(activity)); // Transactional
        }
        return savedActivities;
    }


    // ------------------- Read -------------------
    public List<Activity> getAllActivities() {
        return activityRepository.findAll();
    }

    public Optional<Activity> getActivity(Activity activity) {

        if (activity.getId() != null) {
            return getActivityById(activity.getId());
        } else {
            return getActivityByName(activity.getName());
        }

    }

    // Retrieve activity by id
    public Optional<Activity> getActivityById(Long id) {
        return activityRepository.findById(id);
    }

    // Retrieve activity by name
    public Optional<Activity> getActivityByName(String name) {
        return Optional.ofNullable(activityRepository.findByName(name));
    }


    // ------------------- Update -------------------
        // if exists update, if not create

    @Transactional
    public Activity updateActivity(Activity activity) {
        Optional<Activity> updatedActivity = getActivity(activity);
        if (updatedActivity.isPresent()) {
            deleteActivityById(activity.getId());
        }
        return saveActivity(activity);
    }


    // ------------------- Delete -------------------
        // Delete activity by id
    public void deleteActivityById(Long id) {
        activityRepository.deleteById(id);
    }



}