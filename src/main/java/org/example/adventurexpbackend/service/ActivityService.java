package org.example.adventurexpbackend.service;

import org.example.adventurexpbackend.config.SequenceResetter;
import org.example.adventurexpbackend.model.Activity;
import org.example.adventurexpbackend.model.Booking;
import org.example.adventurexpbackend.model.Equipment;
import org.example.adventurexpbackend.model.EquipmentType;
import org.example.adventurexpbackend.repository.ActivityRepository;
import org.example.adventurexpbackend.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
        System.out.println("Debug: ActivityService: saveActivity");
        System.out.println(" Activity: " + activity);
        multiplyEquipmentTypes(activity);
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


    public Optional<Activity> getActivityById(Long id) {
        System.out.println("Debug: ActivityService: getActivityById: id: " + id);
        Optional<Activity> activity =  activityRepository.findById(id);
        System.out.println(" activity: " + activity);
        return activity;
    }


    public Optional<Activity> getActivityByName(String name) {
        return Optional.ofNullable(activityRepository.findByName(name));
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

    // ------------------- 3. Update -------------------
    @Transactional
    public Activity updateActivity(Activity activity) {
        Optional<Activity> existingActivityOpt = getActivityById(activity.getId());
        if (existingActivityOpt.isPresent()) {
            Activity existingActivity = getActivity(activity, existingActivityOpt);
            existingActivity.getEquipmentList().clear();
            existingActivity.getEquipmentList().addAll(activity.getEquipmentList());
            existingActivity.getEquipmentTypes().clear();
            existingActivity.getEquipmentTypes().addAll(activity.getEquipmentTypes());
            multiplyEquipmentTypes(existingActivity);
            return activityRepository.save(existingActivity);
        } else {
            throw new IllegalArgumentException("Activity not found");
        }
    }

    @Transactional
    public void updateEquipmentList(Long activityId, List<Equipment> newEquipmentList) {
        Activity activity = activityRepository.findById(activityId).orElseThrow(() -> new IllegalArgumentException("Invalid activity id"));
        activity.getEquipmentList().clear();
        activity.getEquipmentList().addAll(newEquipmentList);
        activityRepository.save(activity);
    }

    // ------------------- 4. Delete -------------------
    @Transactional
    public void delete(Activity activity) {
        List<Booking> bookings = bookingRepository.findByActivity(activity);
        bookingRepository.deleteAll(bookings);

        // Verify that all bookings have been deleted
        List<Booking> bookingsDeleted = bookingRepository.findByActivity(activity);

        // If any bookings remain, throw an exception
        if (!bookingsDeleted.isEmpty()) {
            throw new IllegalArgumentException("Activity has bookings");
        }

        // Delete the activity
        activityRepository.delete(activity);
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

    private void multiplyEquipmentTypes(Activity activity) {
        List<Equipment> multipliedEquipmentList = new ArrayList<>();
        for (EquipmentType equipmentType : activity.getEquipmentTypes()) {
            for (int i = 0; i < activity.getPersonsMax(); i++) {
                Equipment equipment = new Equipment(equipmentType.getName(), true, false);
                multipliedEquipmentList.add(equipment);
            }
        }
        activity.setEquipmentList(multipliedEquipmentList);
    }


}