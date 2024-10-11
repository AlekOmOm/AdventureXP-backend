package org.example.adventurexpbackend.controller;

import org.example.adventurexpbackend.model.Activity;
import org.example.adventurexpbackend.model.Booking;
import org.example.adventurexpbackend.model.Equipment;
import org.example.adventurexpbackend.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Activity")
@CrossOrigin(origins = "*")
public class ActivityRESTController {


    @Autowired
    private ActivityService activityService;

    // ------------------- Operations -------------------

    // ------------------- 1. Create -------------------
    @PostMapping
    public ResponseEntity<Activity> createActivity(@RequestBody Activity activity) {
        System.out.println("createActivity");
        System.out.println(" Activity: " + activity);

        activityService.multiplyEquipmentTypes(activity);

        return ResponseEntity.ok(activityService.saveActivity(activity));
    }

    // ------------------- 2. Read -------------------
    @GetMapping
    public ResponseEntity<List<Activity>> getAllActivities() {
        List<Activity> activities = activityService.getAllActivities();

        System.out.println("Retrieving all activities");
        System.out.println("Activities retrieved: " + activities);

        return ResponseEntity.ok(activities);
    }

    @GetMapping("/types")
    public ResponseEntity<List<Activity>> getAllActivityTypes() {
        return ResponseEntity.ok(activityService.getAllActivities());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Activity> getActivityById(@PathVariable Long id) {
        return activityService.checkActivity(activityService.getActivity(id));
    }

    @GetMapping("/{name}")
    public ResponseEntity<Activity> getActivityByName(@PathVariable String name) {
        return activityService.checkActivity(activityService.getActivity(name));
    }

    // ------------------- 3. Update -------------------
    @PutMapping("/{id}")
    public ResponseEntity<Activity> updateActivity(@PathVariable Long id, @RequestBody Activity activity) {
        System.out.println("Updating activity with ID: " + id);
        Optional<Activity> existingActivity = Optional.ofNullable(activityService.updateActivity(activity));
        System.out.println("updateActivity");
        System.out.println(" Activity: " + activity);
        System.out.println(" ExistingActivity: " + existingActivity);

        if (existingActivity.isPresent()) {
            activity.setId(id);
            activity.setEquipmentList(existingActivity.get().getEquipmentList());
            activity.setEquipmentTypes(existingActivity.get().getEquipmentTypes());
            System.out.println(" ActivityUpdated: " + activity);
            Activity updatedActivity = activityService.saveActivity(activity);
            System.out.println("Activity updated: " + updatedActivity);
            return ResponseEntity.ok(updatedActivity);
        } else {
            System.out.println("Activity with ID " + id + " not found");
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/equipment")
    public ResponseEntity<Void> updateEquipmentList(@PathVariable Long id, @RequestBody List<Equipment> newEquipmentList) {
        System.out.println("Updating equipment list for activity with ID: " + id);
        try {
            activityService.updateEquipmentForActivity(id, newEquipmentList);
            System.out.println("Equipment list updated for activity with ID: " + id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            System.out.println("Activity with ID " + id + " not found");
            return ResponseEntity.notFound().build();
        }
    }

    // ------------------- 4. Delete -------------------


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivityById(@PathVariable Long id) {
        activityService.delete(id);
        return ResponseEntity.noContent().build();
    }



}
