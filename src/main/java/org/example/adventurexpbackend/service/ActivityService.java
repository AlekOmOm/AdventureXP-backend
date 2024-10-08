package org.example.adventurexpbackend.service;

import org.example.adventurexpbackend.model.Activity;
import org.example.adventurexpbackend.model.Booking;
import org.example.adventurexpbackend.model.Equipment;
import org.example.adventurexpbackend.repository.ActivityRepository;
import org.example.adventurexpbackend.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    public ActivityService(ActivityRepository activityRepository, BookingRepository bookingRepository) {
        this.activityRepository = activityRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public Activity saveActivity(Activity activity) {
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

    public void delete(Activity activity) {
        List<Booking> bookings = bookingRepository.findByActivity(activity);
        bookingRepository.deleteAll(bookings);
        activityRepository.delete(activity);
    }

    @Transactional
    public void updateEquipmentList(Long activityId, List<Equipment> newEquipmentList) {
        Activity activity = activityRepository.findById(activityId).orElseThrow(() -> new IllegalArgumentException("Invalid activity id"));
        activity.getEquipmentList().clear();
        activity.getEquipmentList().addAll(newEquipmentList);
        activityRepository.save(activity);
    }
}