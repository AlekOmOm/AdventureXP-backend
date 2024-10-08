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