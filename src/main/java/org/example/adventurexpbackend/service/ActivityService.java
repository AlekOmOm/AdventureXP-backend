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

    public Optional<Activity> getActivity(Long id) {
        Activity activity = new Activity();
        activity.setId(id);
        return getActivity(activity);
    }

    public Optional<Activity> getActivity(String name) {
        Activity activity = new Activity();
        activity.setName(name);
        return getActivity(activity);
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


    // ------------------- Delete -------------------

    @Transactional
    public void deleteActivityById(Long id) {
        Optional<Activity> existingActivityOpt = activityRepository.findById(id);
        if (existingActivityOpt.isPresent()) {
            Activity existingActivity = existingActivityOpt.get();
            activityRepository.delete(existingActivity);
        }
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


}


