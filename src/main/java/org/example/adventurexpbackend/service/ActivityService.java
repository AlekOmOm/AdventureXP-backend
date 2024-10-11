package org.example.adventurexpbackend.service;

import org.example.adventurexpbackend.config.SequenceResetter;
import org.example.adventurexpbackend.model.Activity;
import org.example.adventurexpbackend.model.TimeSlot;
import org.example.adventurexpbackend.model.Equipment;
import org.example.adventurexpbackend.model.Booking;
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

    public Optional<Activity> getActivity(Activity activity) {
        return Optional.ofNullable(activity.getId() != null ? activityRepository.findById(activity.getId()).orElse(null) : activityRepository.findByName(activity.getName()));
    }

    public Optional<Activity> getActivity(Long id) {
        Activity activity = new Activity();
        activity.setId(id);
        return getActivity(activity);
    // ------------------- 2. Read -------------------

    public Activity getActivity(Activity activity) {

        if (activity.getId() != null) {
            return activityRepository.findById(activity.getId()).orElse(null);
        } else {
            return activityRepository.findByName(activity.getName());
        }
    }

    public Optional<Activity> getActivity(String name) {
        Activity activity = new Activity();
        activity.setName(name);
        return getActivity(activity);
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
        return new ArrayList<>(activityRepository.findAll());
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
            multiplyEquipmentTypes(existingActivity);
            return activityRepository.save(existingActivity);
        } else {
            throw new IllegalArgumentException("Activity not found");
        }
    }


    private static Activity getActivity(Activity activity, Optional<Activity> existingActivityOpt) {
        if (!existingActivityOpt.isPresent()) {
            throw new IllegalArgumentException("Activity not found");
        }
        return existingActivityOpt.get();
    }

    @Transactional
    public void updateEquipmentForActivity(Long activityId, List<Equipment> newEquipmentList) {
        Activity existingActivity = getActivity(activityId).orElseThrow(() -> new IllegalArgumentException("Invalid activity id"));
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

    @Transactional
    public void delete(long id) {
        Activity activity = new Activity();
        activity.setId(id);
        delete(activity);
    }

    @Transactional
    public void delete(String name) {
        Activity activity = new Activity();
        activity.setName(name);
        delete(activity);
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

    @Transactional
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

    public Activity bookAndSave(Activity frontendActivity) {
        // assumes that
            // timeslots have been booked
            // timeslot current participants have been updated

        // 1 step - find timeslots booked
            // get activity from repo to compare
        Optional<Activity> repoActivity = getActivity(frontendActivity);
        if (repoActivity.isEmpty()) {
            return null;
        }

        if (frontendActivity.equals(repoActivity.get())) {
            return null;
        }

        // 2 step - book the timeslots
        List<TimeSlot> timeSlots = frontendActivity.getTimeSlots();
        List<TimeSlot> repoTimeSlots = repoActivity.get().getTimeSlots();

        for (TimeSlot timeSlot : timeSlots) {
            for (TimeSlot repoTimeSlot : repoTimeSlots) {
                if (timeSlot.equals(repoTimeSlot)) {
                    repoTimeSlot.setCurrentParticipants(timeSlot.getCurrentParticipants());
                }
            }
        }

        repoActivity.get().setTimeSlots(repoTimeSlots);

        // 3 step - save
        return activityRepository.save(repoActivity.get());
    }


    // ------------------- 5. Other -------------------
    @Transactional
    public void updateEquipmentList(Long activityId, List<Equipment> newEquipmentList) {
        Activity activity = activityRepository.findById(activityId).orElseThrow(() -> new IllegalArgumentException("Invalid activity id"));
        activity.getEquipmentList().clear();
        activity.getEquipmentList().addAll(newEquipmentList);
        activityRepository.save(activity);
    }

}


