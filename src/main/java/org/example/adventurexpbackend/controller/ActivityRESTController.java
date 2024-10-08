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
    public ResponseEntity<List<Activity>>getAllActivities(){
        List<Activity> activities = activityService.getAllActivities();
        System.out.println(activities);
        return ResponseEntity.ok(activities);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Activity> updateActivity(@PathVariable Long id, @RequestBody Activity activity) {
        Optional<Activity> existingActivity = Optional.ofNullable(activityService.getActivity(activity));
        System.out.println("updateActivity");
        System.out.println(" Activity: " + activity);
        System.out.println(" ExistingActivity: " + existingActivity);
        if (existingActivity.isPresent()) {
            activity.setId(id);
            activity.setEquipmentList(existingActivity.get().getEquipmentList());
            activity.setEquipmentTypes(existingActivity.get().getEquipmentTypes());
            System.out.println(" ActivityUpdated: " + activity);
            Activity updatedActivity = activityService.saveActivity(activity);
            return ResponseEntity.ok(updatedActivity);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/equipment")
    public ResponseEntity<Void> updateEquipmentList(@PathVariable Long id, @RequestBody List<Equipment> newEquipmentList) {
        try {
            activityService.updateEquipmentList(id, newEquipmentList);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint to retrieve an activity by id
    @GetMapping("/{id}")
    public ResponseEntity<Activity> getActivityById(@PathVariable Long id) {
        Activity activity = new Activity();
        activity.setId(id);
        Optional<Activity> existingActivity = Optional.ofNullable(activityService.getActivity(activity));
        return existingActivity.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint to retrieve an activity by name
    @GetMapping("/name/{name}")
    public ResponseEntity<Activity> getActivityByName(@PathVariable String name) {
        Activity activity = new Activity();
        activity.setName(name);
        Optional<Activity> existingActivity = Optional.ofNullable(activityService.getActivity(activity));
        return existingActivity.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint to delete an activity by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivityById(@PathVariable Long id) {
        Activity activity = new Activity();
        activity.setId(id);
        activityService.delete(activity);
        return ResponseEntity.noContent().build();
    }

}
