package org.example.adventurexpbackend.controller;

import org.example.adventurexpbackend.model.Activity;
import org.example.adventurexpbackend.model.Equipment;
import org.example.adventurexpbackend.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Activity")
@CrossOrigin(origins = "*")
public class ActivityRESTController {


    @Autowired
    private ActivityService activityService;

    // Endpoint to create a new activity
    @PostMapping
    public ResponseEntity<Activity> createActivity(@RequestBody Activity activity) {
        System.out.println("createActivity");
        System.out.println(" Activity: " + activity);
        Activity savedActivity = activityService.saveActivity(activity);
        return ResponseEntity.ok(savedActivity);
    }

    // Endpoint to retrieve all activities
    @GetMapping
    public ResponseEntity<List<Activity>> getAllActivities() {
        System.out.println("Retrieving all activities");
        List<Activity> activities = activityService.getAllActivities();
        System.out.println("Activities retrieved: " + activities);
        return ResponseEntity.ok(activities);
    }

    @GetMapping("/types")
    public ResponseEntity<List<Activity>> getAllActivityTypes() {
        return ResponseEntity.ok(activityService.getAllActivities());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Activity> updateActivity(@PathVariable Long id, @RequestBody Activity activity) {
        System.out.println("Updating activity with ID: " + id);
        Optional<Activity> existingActivity = Optional.ofNullable(activityService.updateActivityFromExistent(activity));
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

    // Endpoint to retrieve an activity by id
    @GetMapping("/{id}")
    public ResponseEntity<Activity> getActivityById(@PathVariable Long id) {
        System.out.println("Retrieving activity with ID: " + id);
        Activity activity = new Activity();
        activity.setId(id);
        Optional<Activity> existingActivity = Optional.ofNullable(activityService.updateActivityFromExistent(activity));
        if (existingActivity.isPresent()) {
            System.out.println("Activity retrieved: " + existingActivity.get());
            return ResponseEntity.ok(existingActivity.get());
        } else {
            System.out.println("Activity with ID " + id + " not found");
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint to retrieve an activity by name
    @GetMapping("/name/{name}")
    public ResponseEntity<Activity> getActivityByName(@PathVariable String name) {
        System.out.println("Retrieving activity with name: " + name);
        Activity activity = new Activity();
        activity.setName(name);
        Optional<Activity> existingActivity = Optional.ofNullable(activityService.updateActivityFromExistent(activity));
        if (existingActivity.isPresent()) {
            System.out.println("Activity retrieved: " + existingActivity.get());
            return ResponseEntity.ok(existingActivity.get());
        } else {
            System.out.println("Activity with name " + name + " not found");
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint to delete an activity by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivityById(@PathVariable Long id) {
        System.out.println("Deleting activity with ID: " + id);
        Activity activity = new Activity();
        activity.setId(id);
        activityService.delete(activity);
        System.out.println("Activity with ID " + id + " deleted");
        return ResponseEntity.noContent().build();
    }

}
