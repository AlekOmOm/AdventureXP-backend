package org.example.adventurexpbackend.config.initData;

import org.example.adventurexpbackend.model.Activity;
import org.example.adventurexpbackend.model.Equipment;
import org.example.adventurexpbackend.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InitDataEquipment {
    private List<Activity> activities;
    private final EquipmentRepository equipmentRepository;
    private final List<Equipment> paintballEquipmentList = new ArrayList<>();
    private final List<Equipment> climbingEquipmentList = new ArrayList<>();
    private final List<Equipment> goKartEquipmentList = new ArrayList<>();

    @Autowired
    public InitDataEquipment(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    public void saveData() {
        equipmentRepository.saveAll(getEquipmentList());
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    // Equipment is for the activities: Paintball, Climbing, Go-kart


    public List<Equipment> getEquipmentList() {
        List<Equipment> equipmentList = new ArrayList<>();
        for (Activity activity : activities) {
            int maxCapacity = activity.getPersonsMax();
            for (int i = 0; i< maxCapacity; i++) {
                switch (activity.getName()) {
                    case "Paintball" -> paintballEquipmentList.addAll(getPaintballEquipmentSet());
                    case "Climbing" -> climbingEquipmentList.addAll(getClimbingEquipmentSet());
                    case "Go-kart" -> goKartEquipmentList.addAll(getGoKartEquipmentSet());
                }
            }
        }

        equipmentList.addAll(paintballEquipmentList);
        equipmentList.addAll(climbingEquipmentList);
        equipmentList.addAll(goKartEquipmentList);

        return equipmentList;
    }

    // ----------------- Equipment Lists for Activities ---------------------

    public List<Equipment> getPaintBallEquipmentList() {
        return paintballEquipmentList;
    }

    public List<Equipment> getClimbingEquipmentList() {
        return climbingEquipmentList;
    }

    public List<Equipment> getGoKartEquipmentList() {
        return goKartEquipmentList;
    }


    // ----------------- Equipment Sets ---------------------

    public Set<Equipment> getGoKartEquipmentSet() {
        Activity goKartActivity = new Activity("Go-kart", "Go-kart is a fun activity for everyone", 100, 120, 10, 100, 2, 20, null, null, 2, null, null);

        return new HashSet<>(List.of(
                new Equipment("Go-kart helmet", true, false, goKartActivity),
                new Equipment("Go-kart suit", true, false, goKartActivity),
                new Equipment("Go-kart gloves", true, false, goKartActivity)
        ));
    }

    public Set<Equipment> getClimbingEquipmentSet() {
        Activity climbingActivity = new Activity("Climbing", "Climbing is a fun activity for everyone", 100, 120, 10, 100, 2, 20, null, null, 2, null, null);

        return new HashSet<>(List.of(
                new Equipment("Climbing shoes", true, false, climbingActivity),
                new Equipment("Climbing harness", true, false, climbingActivity),
                new Equipment("Climbing chalk", true, false, climbingActivity)
        ));
    }

    public Set<Equipment> getPaintballEquipmentSet() {
        Activity paintballActivity = new Activity("Paintball", "Paintball is a fun activity for everyone", 100, 120, 10, 100, 2, 20, null, null, 2, null, null);

        return new HashSet<>(List.of(
                new Equipment("Paintball gun", true, false, paintballActivity),
                new Equipment("Paintball mask", true, false, paintballActivity),
                new Equipment("Paintball suit", true, false, paintballActivity)
        ));
    }


}
