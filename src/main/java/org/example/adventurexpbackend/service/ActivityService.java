package org.example.adventurexpbackend.service;

import org.example.adventurexpbackend.config.SequenceResetter;
import org.example.adventurexpbackend.model.Activity;
import org.example.adventurexpbackend.model.TimeSlot;
import org.example.adventurexpbackend.model.Equipment;
import org.example.adventurexpbackend.model.Booking;

import org.example.adventurexpbackend.repository.ActivityRepository;
import org.example.adventurexpbackend.repository.BookingRepository;
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
    private final BookingRepository bookingRepository;

    @Autowired
    public ActivityService(ActivityRepository activityRepository, BookingRepository bookingRepository,  SequenceResetter sequenceResetter) {
        this.activityRepository = activityRepository;
        this.sequenceResetter = sequenceResetter;
        this.bookingRepository = bookingRepository;
    }

    // ------------------- Operations -------------------

    // ------------------- 1. Create -------------------
    @Transactional
    public Activity saveActivity(Activity activity) {
        return activityRepository.save(activity);
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


    // ------------------- 2. Read -------------------

    public Optional<Activity> getActivity(Activity activity) {
        return Optional.ofNullable(activity.getId() != null ? activityRepository.findById(activity.getId()).orElse(null) : activityRepository.findByName(activity.getName()));
    }

    public List<Activity> getAllActivities() {
        return new ArrayList<>(activityRepository.findAll());
    }

    public Optional<Activity> getActivityById(Long id) {
        return activityRepository.findById(id);
    }

    // ------------------- 3. Update -------------------

    private static Activity updateActivityFromExistent(Activity activity, Optional<Activity> existingActivityOpt) {
        if (existingActivityOpt.isEmpty()) {
            throw new IllegalArgumentException("Activity not found");
        }
        Activity existingActivity = existingActivityOpt.get();
        existingActivity.updateFrom(activity);
        return existingActivity;
    }


    @Transactional
    public Activity updateActivity(Activity activity) {
        Optional<Activity> existingActivityOpt = getActivity(activity);
        if (existingActivityOpt.isPresent()) {
            Activity existingActivity = updateActivityFromExistent(activity, existingActivityOpt);
            existingActivity.getEquipmentList().clear();
            existingActivity.getEquipmentList().addAll(activity.getEquipmentList());
            existingActivity.getEquipmentTypes().clear();
            existingActivity.getEquipmentTypes().addAll(activity.getEquipmentTypes());
            return activityRepository.save(existingActivity);
        } else {
            throw new IllegalArgumentException("Activity not found");
        }
    }

    @Transactional
    public void updateEquipmentForActivity(Long activityId, List<Equipment> newEquipmentList) {
        Activity existingActivity = getActivityById(activityId).orElseThrow(() -> new IllegalArgumentException("Invalid activity id"));
        existingActivity.setEquipmentList(newEquipmentList);
        activityRepository.save(existingActivity);
    }

    // ------------------- 4. Delete -------------------

    @Transactional
    public void delete(Activity activity) {
        List<Booking> bookings = bookingRepository.findByActivity(activity);
        bookingRepository.deleteAll(bookings);
        activityRepository.delete(activity);
    }


    // Delete activity by id
    public void deleteActivityById(Long id) {
        Optional<Activity> existingActivityOpt = activityRepository.findById(id);
        if (existingActivityOpt.isPresent()) {
            Activity existingActivity = existingActivityOpt.get();
            activityRepository.delete(existingActivity);
        }
    }



    // ------------------- 5. Other -------------------


}


