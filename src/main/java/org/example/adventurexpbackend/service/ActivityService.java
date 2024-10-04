package org.example.adventurexpbackend.service;

import org.example.adventurexpbackend.model.Activity;
import org.example.adventurexpbackend.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;  // Injecting the ActivityRepository dependency


    // Create or Update activity
    public Activity saveActivity(Activity activity) {
        return activityRepository.save(activity);
    }

    // Retrieve all activities
    public List<Activity> getAllActivities() {
        return activityRepository.findAll();
    }

    // Retrieve activity by id
    public Optional<Activity> getActivityById(Long id) {
        return activityRepository.findById(id);
    }

    // Retrieve activity by name
    public Optional<Activity> getActivityByName(String name) {
        return Optional.ofNullable(activityRepository.findByName(name));
    }

    // Delete activity by id
    public void deleteActivityById(Long id) {
        activityRepository.deleteById(id);
    }

}
