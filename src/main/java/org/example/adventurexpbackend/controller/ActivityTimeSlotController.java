package org.example.adventurexpbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.adventurexpbackend.model.Activity;
import org.example.adventurexpbackend.model.TimeSlot;
import org.example.adventurexpbackend.service.ActivityService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/activities")
public class ActivityTimeSlotController {

    private final ActivityService activityService;

    @Autowired
    public ActivityTimeSlotController(ActivityService activityService) {
        this.activityService = activityService;
    }

    //all available timeslots
    @GetMapping("/{activityId}/available-timeslots")
    public ResponseEntity<List<TimeSlot>> getAvailableTimeSlots(@PathVariable Long activityId) {
        Optional<Activity> activityOptional = activityService.findActivityById(activityId);
        if (activityOptional.isPresent()) {
            Activity activity = activityOptional.get();
            // Here we stream that we only get the is availbale.
            List<TimeSlot> availableTimeSlots = activity.getTimeSlots().stream()
                    .filter(TimeSlot::isAvailable)
                    .collect(Collectors.toList());

            return new ResponseEntity<>(availableTimeSlots, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //
    //to book a timeslot and mark it as unavailable
    @PostMapping("/{activityId}/book-timeslot/{timeslotId}")
    public ResponseEntity<String> bookTimeSlot(@PathVariable Long activityId, @PathVariable Long timeslotId) {
        Optional<Activity> activityOptional = activityService.findActivityById(activityId);
        if (activityOptional.isPresent()) {
            Activity activity = activityOptional.get();
            // here we search/find for a timeslot to book
            Optional<TimeSlot> timeSlotOptional = activity.getTimeSlots().stream()
                    .filter(ts -> ts.getId().equals(timeslotId))
                    .findFirst();

            if (timeSlotOptional.isPresent()) {
                TimeSlot timeSlot = timeSlotOptional.get();
                if (timeSlot.isAvailable()) {
                    //We set the timeslot as unavailable
                    timeSlot.setAvailable(false);
                    activityService.saveActivity(activity); // Save changes
                    return new ResponseEntity<>("Timeslot booked successfully :-).", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Timeslot is already booked. :-(", HttpStatus.BAD_REQUEST);
                }
            }
            return new ResponseEntity<>("Timeslot not found #Sad.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Activity not found. Much bigger problem", HttpStatus.NOT_FOUND);
    }
}
