package org.example.adventurexpbackend.config.initData;

import org.example.adventurexpbackend.model.Activity;
import org.example.adventurexpbackend.model.Equipment;
import org.example.adventurexpbackend.repository.ActivityRepository;
import org.example.adventurexpbackend.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
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
        List<Equipment> paintballEquipmentList = initDataEquipment.getPaintBallEquipmentList();
        List<Equipment> climbingEquipmentList = initDataEquipment.getClimbingEquipmentList();
        List<Equipment> goKartEquipmentList = initDataEquipment.getGoKartEquipmentList();

        return new ArrayList<>(List.of(
                new Activity("Paintball", "Paintball is a fun activity for everyone", 100, 120, 10, 100, 2, 20, LocalTime.of(10, 0), LocalTime.of(18,0), 2, paintballEquipmentList, initDataEquipment.getPaintballEquipmentSet()),
                new Activity("Climbing", "Climbing is a fun activity for everyone", 100, 120, 10, 100, 2, 20, LocalTime.of(10, 0), LocalTime.of(18,0), 2, climbingEquipmentList, initDataEquipment.getClimbingEquipmentSet()),
                new Activity("Go-kart", "Go-kart is a fun activity for everyone", 100, 120, 10, 100, 2, 20, LocalTime.of(10, 0), LocalTime.of(18,0), 2, goKartEquipmentList, initDataEquipment.getGoKartEquipmentSet())
        ));
    }

    public List<Activity> saveData() {
        return activityService.saveAllActivities(getActivities());
    }


}
