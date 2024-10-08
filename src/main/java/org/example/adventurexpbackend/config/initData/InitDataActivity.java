package org.example.adventurexpbackend.config.initData;

import org.example.adventurexpbackend.model.Activity;
import org.example.adventurexpbackend.model.Equipment;
import org.example.adventurexpbackend.model.EquipmentType;
import org.example.adventurexpbackend.repository.ActivityRepository;
import org.example.adventurexpbackend.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class InitDataActivity {

    private final InitDataEquipment initDataEquipment;
    private final ActivityRepository activityRepository;
    private final ActivityService activityService;

    @Autowired
    public InitDataActivity(InitDataEquipment initDataEquipment, ActivityRepository activityRepository, ActivityService activityService) {
        this.initDataEquipment = initDataEquipment;
        this.activityRepository = activityRepository;
        this.activityService = activityService;
    }

    protected List<Activity> getActivities() {

        List<Activity> activities = new ArrayList<>(List.of(
                new Activity("Paintball", "Paintball is a fun activity for everyone", 100, 120, 10, 100, 2, 20, LocalTime.of(10, 0), LocalTime.of(18,0), 60, initDataEquipment.getPaintBallEquipmentList(), initDataEquipment.getPaintballEquipmentTypes()),
                new Activity("Climbing", "Climbing is a fun activity for everyone", 100, 120, 10, 100, 2, 20, LocalTime.of(10, 0), LocalTime.of(18,0), 60, initDataEquipment.getClimbingEquipmentList(), initDataEquipment.getClimbingEquipmentTypes()),
                new Activity("Go-kart", "Go-kart is a fun activity for everyone", 100, 120, 10, 100, 2, 20, LocalTime.of(10, 0), LocalTime.of(18,0), 60, initDataEquipment.getGoKartEquipmentList(), initDataEquipment.getGoKartEquipmentTypes())
        ));


        // Set activity in equipment and equipment types
        for (Activity activity : activities) {
            for (Equipment equipment : activity.getEquipmentList()) {
                equipment.setActivity(activity);
            }
            for (EquipmentType equipmentType : activity.getEquipmentTypes()) {
                equipmentType.setActivity(activity);
            }
        }

        System.out.println("Debug: getActivities");
        System.out.println(" Activities: " + activities);
        return activities;
    }

    public List<Activity> saveData() {
        return activityService.saveAllActivities(getActivities());
    }
}