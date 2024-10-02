package org.example.adventurexpbackend.config.initData;

import org.example.adventurexpbackend.model.Activity;
import org.example.adventurexpbackend.model.Equipment;
import org.example.adventurexpbackend.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class InitDataActivity {
    // Activity is for the activities: Paintball, Climbing, Go-kart

    // constructor parameters: String name, String description, int pricePrPerson, int timeMaxLimit, int ageMin, int ageMax, int personsMin, int personsMax, LocalTime openingTime, LocalTime closingTime, int timeSlotInterval, List<Equipment> equipmentList, Set<Equipment> equipmentRequiredPerPerson) {

    private final InitDataEquipment initDataEquipment;
    private final ActivityRepository activityRepository;

    @Autowired
    public InitDataActivity(InitDataEquipment initDataEquipment, ActivityRepository activityRepository) {
        this.initDataEquipment = initDataEquipment;
        this.activityRepository = activityRepository;
    }


    protected List<Activity> getActivities() {
        return new ArrayList<>(List.of(
                new Activity("Paintball", "Paintball is a fun activity for everyone", 100, 120, 10, 100, 2, 20, 10, 18, 2, initDataEquipment.getPaintBallEquipmentList(), initDataEquipment.getPaintBallEquipmentRequiredPerPerson()),
                new Activity("Climbing", "Climbing is a fun activity for everyone", 100, 120, 10, 100, 2, 20, 10, 18, 2, initDataEquipment.getClimbingEquipmentList(), initDataEquipment.getClimbingEquipmentRequiredPerPerson()),
                new Activity("Go-kart", "Go-kart is a fun activity for everyone", 100, 120, 10, 100, 2, 20, 10, 18, 2, initDataEquipment.getGoKartEquipmentList(), initDataEquipment.getGoKartEquipmentRequiredPerPerson())
        ));
    }

    public void saveData() {
        activityRepository.saveAll(getActivities());
        initDataEquipment.saveData(getActivities());

    }


}
