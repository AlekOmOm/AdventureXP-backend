package org.example.adventurexpbackend.config.initData;

import org.example.adventurexpbackend.model.Activity;
import org.example.adventurexpbackend.model.Equipment;
import org.example.adventurexpbackend.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<Equipment> getPaintBallEquipmentList() {
        return paintballEquipmentList;
    }

    public List<Equipment> getClimbingEquipmentList() {
        return climbingEquipmentList;
    }

    public List<Equipment> getGoKartEquipmentList() {
        return goKartEquipmentList;
    }

    public Set<Equipment> getGoKartEquipmentSet() {
        return new HashSet<>(List.of(
                new Equipment("Go-kart helmet", true, false, null),
                new Equipment("Go-kart suit", true, false, null),
                new Equipment("Go-kart gloves", true, false, null)
        ));
    }

    public Set<Equipment> getClimbingEquipmentSet() {
        return new HashSet<>(List.of(
                new Equipment("Climbing shoes", true, false, null),
                new Equipment("Climbing harness", true, false, null),
                new Equipment("Climbing chalk", true, false, null)
        ));
    }

    public Set<Equipment> getPaintballEquipmentSet() {
        return new HashSet<>(List.of(
                new Equipment("Paintball gun", true, false, null),
                new Equipment("Paintball mask", true, false, null),
                new Equipment("Paintball suit", true, false, null)
        ));
    }
}