package org.example.adventurexpbackend.config.initData;

import org.example.adventurexpbackend.model.Activity;
import org.example.adventurexpbackend.model.Equipment;
import org.example.adventurexpbackend.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
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
        this.activities = activities;

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
        return new HashSet<>(List.of(
                new Equipment("Go-kart", true, false, activities.get(2)),
                new Equipment("Go-kart helmet", true, false, activities.get(2)),
                new Equipment("Go-kart suit", true, false, activities.get(2))
        ));
    }

    public Set<Equipment> getClimbingEquipmentSet() {
        return new HashSet<>(List.of(
                new Equipment("Climbing shoes", true, false, activities.get(1)),
                new Equipment("Climbing harness", true, false, activities.get(1)),
                new Equipment("Climbing rope", true, false, activities.get(1))
        ));
    }

    public Set<Equipment> getPaintballEquipmentSet() {
        return new HashSet<>(List.of(
                new Equipment("Paintball gun", true, false, activities.getFirst()),
                new Equipment("Paintball mask", true, false, activities.getFirst()),
                new Equipment("Paintball suit", true, false, activities.getFirst())
        ));
    }


}
