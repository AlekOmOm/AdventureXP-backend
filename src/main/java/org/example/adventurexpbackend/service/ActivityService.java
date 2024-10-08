package org.example.adventurexpbackend.service;

import org.example.adventurexpbackend.config.SequenceResetter;
import org.example.adventurexpbackend.model.Activity;
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
    private final SequenceResetter sequenceResetter;

    @Autowired
    public ActivityService(ActivityRepository activityRepository, SequenceResetter sequenceResetter) {
        this.activityRepository = activityRepository;
        this.sequenceResetter = sequenceResetter;
    }

    // ------------------- Create -------------------
    @Transactional
    public Activity saveActivity(Activity activity) {
        return activityRepository.save(activity);
    }



    public List<Activity> getAllActivities() {
        List <Activity> activities = new ArrayList<>();
        System.out.println("Debug: Activity:");
        for (Activity activity : activityRepository.findAll()) {
            activities.add(activity);

            System.out.println( " " + activity);
        }
        return activities;
    }

    @Transactional
    public List<Activity> saveAllActivities(List<Activity> activities) {
        List<Activity> savedActivities = new ArrayList<>();

        List<Activity> repoList = activityRepository.findAll();
        if (!repoList.isEmpty()) {
            sequenceResetter.resetAutoIncrement("activity", repoList.getLast().getId() + 1);
        }

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

    public void delete(Activity activity) {
        activityRepository.delete(activity);
    }


    // ------------------- Read -------------------


    public Optional<Activity> getActivityOpt(Activity activity) {

        if (activity.getId() != null) {
            return getActivityById(activity.getId());
        } else {
            return getActivityByName(activity.getName());
        }

    }

    // Retrieve activity by id
    public Optional<Activity> getActivityById(Long id) {
        System.out.println("Debug: ActivityService: getActivityById: id: " + id);
        Optional<Activity> activity =  activityRepository.findById(id);
        System.out.println(" activity: " + activity);
        return activity;
    }

    // Retrieve activity by name
    public Optional<Activity> getActivityByName(String name) {
        return Optional.ofNullable(activityRepository.findByName(name));
    }


    // ------------------- Update -------------------
        // if exists update, if not create

    @Transactional
    public Activity updateActivity(Activity activity) {
        Optional<Activity> existingActivityOpt = getActivityById(activity.getId());
        if (existingActivityOpt.isPresent()) {
            Activity existingActivity = getActivity(activity, existingActivityOpt);
            existingActivity.getEquipmentList().clear();
            existingActivity.getEquipmentList().addAll(activity.getEquipmentList());
            existingActivity.getEquipmentTypes().clear();
            existingActivity.getEquipmentTypes().addAll(activity.getEquipmentTypes());
            return activityRepository.save(existingActivity);
        } else {
            throw new IllegalArgumentException("Activity not found");
        }
    }

    private static Activity getActivity(Activity activity, Optional<Activity> existingActivityOpt) {
        if (!existingActivityOpt.isPresent()) {
            throw new IllegalArgumentException("Activity not found");
        }

        Activity existingActivity = existingActivityOpt.get();
        existingActivity.setName(activity.getName());
        existingActivity.setDescription(activity.getDescription());
        existingActivity.setPricePrPerson(activity.getPricePrPerson());
        existingActivity.setTimeMaxLimit(activity.getTimeMaxLimit());
        existingActivity.setAgeMin(activity.getAgeMin());
        existingActivity.setAgeMax(activity.getAgeMax());
        existingActivity.setPersonsMin(activity.getPersonsMin());
        existingActivity.setPersonsMax(activity.getPersonsMax());
        existingActivity.setOpeningTime(activity.getOpeningTime());
        existingActivity.setClosingTime(activity.getClosingTime());
        existingActivity.setTimeSlotInterval(activity.getTimeSlotInterval());
        existingActivity.setEquipmentList(activity.getEquipmentList());
        existingActivity.setEquipmentTypes(activity.getEquipmentTypes());

        return existingActivity;
    }


    // ------------------- Delete -------------------
        // Delete activity by id
    public void deleteActivityById(Long id) {
        Optional<Activity> existingActivityOpt = activityRepository.findById(id);
        if (existingActivityOpt.isPresent()) {
            Activity existingActivity = existingActivityOpt.get();

            activityRepository.delete(existingActivity);
        }
    }



}