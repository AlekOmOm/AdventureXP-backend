package org.example.adventurexpbackend.service;

import org.example.adventurexpbackend.config.SequenceResetter;
import org.example.adventurexpbackend.model.Activity;
import org.example.adventurexpbackend.model.TimeSlot;
import org.example.adventurexpbackend.model.Booking;
import org.example.adventurexpbackend.model.Equipment;
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
    public ActivityService(ActivityRepository activityRepository, BookingRepository bookingRepository, SequenceResetter sequenceResetter) {
        this.activityRepository = activityRepository;
        this.sequenceResetter = sequenceResetter;
        this.bookingRepository = bookingRepository;
    }

    // ------------------- Create -------------------

    @Transactional
    public Activity saveActivity(Activity activity) {
        return activityRepository.save(activity);
    }

    @Transactional
    public List<Activity> saveAllActivities(List<Activity> activities) {
        List<Activity> savedActivities = new ArrayList<>();

        List<Activity> repoList = activityRepository.findAll();
        if (!repoList.isEmpty()) {
            sequenceResetter.resetAutoIncrement("activity", repoList.get(repoList.size() - 1).getId() + 1);
        }

        for (Activity activity : activities) {
            savedActivities.add(saveActivity(activity));
        }
        return savedActivities;
    }

    @Transactional
    public void updateEquipmentList(Long activityId, List<Equipment> newEquipmentList) {
        Activity activity = activityRepository.findById(activityId).orElseThrow(() -> new IllegalArgumentException("Invalid activity id"));
        activity.getEquipmentList().clear();
        activity.getEquipmentList().addAll(newEquipmentList);
        activityRepository.save(activity);
    }

    @Transactional
    public void delete(Activity activity) {
        List<Booking> bookings = bookingRepository.findByActivity(activity);
        bookingRepository.deleteAll(bookings);
        activityRepository.delete(activity);
    }

    // ------------------- Read -------------------

    public Optional<Activity> getActivityById(Long id) {
        return activityRepository.findById(id);
    }

    public Optional<Activity> getActivityByName(String name) {
        return Optional.ofNullable(activityRepository.findByName(name));
    }

    public List<Activity> getAllActivities() {
        List<Activity> activities = new ArrayList<>();
        for (Activity activity : activityRepository.findAll()) {
            activities.add(activity);
        }
        return activities;
    }

    public Activity getActivity(Activity activity) {
        if (activity.getId() != null) {
            return activityRepository.findById(activity.getId()).orElse(null);
        } else {
            return activityRepository.findByName(activity.getName());
        }
    }

    public Optional<Activity> getActivityOpt(Activity activity) {
        if (activity.getId() != null) {
            return getActivityById(activity.getId());
        } else {
            return getActivityByName(activity.getName());
        }
    }

    // ------------------- Update -------------------
    @Transactional
    public Activity updateActivity(Activity activity) {
        Optional<Activity> existingActivityOpt = getActivityById(activity.getId());
        if (existingActivityOpt.isPresent()) {
            Activity existingActivity = updateExistingActivity(activity, existingActivityOpt.get());
            return activityRepository.save(existingActivity);
        } else {
            throw new IllegalArgumentException("Activity not found");
        }
    }

    private static Activity updateExistingActivity(Activity activity, Activity existingActivity) {
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

    @Transactional
    public void deleteActivityById(Long id) {
        Optional<Activity> existingActivityOpt = activityRepository.findById(id);
        existingActivityOpt.ifPresent(activityRepository::delete);
    }

    // Helper for updating timeslots
    private void loadTimeslots(TimeSlot timeSlot) {
        if (timeSlot.isAvailable()) {
            timeSlot.setAvailable(false);
        }
    }
}
