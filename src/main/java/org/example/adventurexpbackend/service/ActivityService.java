package org.example.adventurexpbackend.service;

import org.example.adventurexpbackend.model.Activity;
import org.example.adventurexpbackend.model.TimeSlot;
import org.example.adventurexpbackend.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ActivityService {

    public Optional<Activity> findActivityById(Long id) {
        return activityRepository.findById(id);
    }

    private final ActivityRepository activityRepository;

    @Autowired
    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Transactional
    public Activity saveActivity(Activity activity) {
        List<TimeSlot> generatedTimeSlots = generateTimeSlots(activity);
        activity.setTimeSlots(generatedTimeSlots);

        for (TimeSlot timeSlot : generatedTimeSlots) {
            updateTimeSlotAvailability(timeSlot);
        }

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



    //----------------------------------------------------------------------------------------------------------------
  //Timeslot generator :-)
    private List<TimeSlot> generateTimeSlots(Activity activity) {
        List<TimeSlot> timeSlots = new ArrayList<>();

        // open and close from 10 to 18
        LocalTime openingTime = LocalTime.of(10, 0);
        LocalTime closingTime = LocalTime.of(18, 0);
        int interval = activity.getTimeSlotInterval();

        // MAking the slots
        LocalTime currentTime = openingTime;
        while (currentTime.isBefore(closingTime)) {
            LocalTime endTime = currentTime.plusMinutes(interval);
            if (endTime.isAfter(closingTime)) {
                endTime = closingTime;
            }

            TimeSlot timeSlot = new TimeSlot();
            timeSlot.setStartTime(currentTime);
            timeSlot.setEndTime(endTime);


            timeSlot.setMaxParticipants(activity.getPersonsMax());
            timeSlot.setCurrentParticipants(0);
            timeSlot.setAvailable(true);

            timeSlots.add(timeSlot);

            currentTime = endTime;
        }

        return timeSlots;
    }
//----------------------------------------------------------------------------------------------------------------------
    private void loadTimeslots(TimeSlot timeSlot){
        if (timeSlot.isAvailable()){
            timeSlot.setAvailable(false);
        }
    }


    //Setting the availability based on participans
    private void updateTimeSlotAvailability(TimeSlot timeSlot) {
        if (timeSlot.getCurrentParticipants() >= timeSlot.getMaxParticipants()) {
            timeSlot.setAvailable(false);
        } else {
            timeSlot.setAvailable(true);
        }
    }
}
