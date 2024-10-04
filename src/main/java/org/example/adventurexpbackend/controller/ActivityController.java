package org.example.adventurexpbackend.controller;


import org.example.adventurexpbackend.model.Activity;
import org.example.adventurexpbackend.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Activity")
@CrossOrigin(origins = "*")
public class ActivityController {


    @Autowired
    private ActivityService activityService;

    // Endpoint to create or update an activity
    @PostMapping
    public ResponseEntity<Activity> createOrUpdateActivity(@RequestBody Activity activity) {
        Activity savedActivity = activityService.saveActivity(activity);
        return ResponseEntity.ok(savedActivity);
    }

    // Endpoint to retrieve all activities
    @GetMapping
    public ResponseEntity<List<Activity>>getAllActivities(){
        List<Activity> activities = activityService.getAllActivities();
        return ResponseEntity.ok(activities);
    }

    // Endpoint to retrieve an activity by id
    @GetMapping("/{id}")
    public ResponseEntity<Activity> getActivityById(@PathVariable Long id) {
        Optional<Activity> activity = activityService.getActivityById(id);
        return activity.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint to retrieve an activity by name
    @GetMapping("/name/{name}")
    public ResponseEntity<Activity> getActivityByName(@PathVariable String name) {
        Optional<Activity> activity = activityService.getActivityByName(name);
        return activity.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint to delete an activity by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivityById(@PathVariable Long id) {
        activityService.deleteActivityById(id);
        return ResponseEntity.noContent().build();
    }

}
