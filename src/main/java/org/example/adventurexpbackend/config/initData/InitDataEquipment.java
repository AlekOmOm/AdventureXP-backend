package org.example.adventurexpbackend.config.initData;

import org.example.adventurexpbackend.model.Activity;
import org.example.adventurexpbackend.model.Equipment;
import org.example.adventurexpbackend.model.EquipmentType;
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
                    case "Paintball" -> paintballEquipmentList.addAll(getPaintballEquipmentTypeList());
                    case "Climbing" -> climbingEquipmentList.addAll(getClimbingEquipmentTypeList());
                    case "Go-kart" -> goKartEquipmentList.addAll(getGoKartEquipmentTypeList());
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

    public Set<EquipmentType> getGoKartEquipmentTypes() {
        return new HashSet<>(List.of(
                new EquipmentType("Go-kart car"),
                new EquipmentType("Go-kart helmet"),
                new EquipmentType("Go-kart suit"),
                new EquipmentType("Go-kart gloves")
        ));
    }

    public Set<EquipmentType> getClimbingEquipmentTypes() {
        return new HashSet<>(List.of(
                new EquipmentType("Climbing shoes"),
                new EquipmentType("Climbing harness"),
                new EquipmentType("Climbing chalk")
        ));
    }

    public Set<EquipmentType> getPaintballEquipmentTypes() {
        return new HashSet<>(List.of(
                new EquipmentType("Paintball gun"),
                new EquipmentType("Paintball mask"),
                new EquipmentType("Paintball suit")
        ));
    }



    // convert types to Equipment objects

    public List<Equipment> getPaintballEquipmentTypeList() {

        List<Equipment> equipmentList = new ArrayList<>();
        for (EquipmentType equipmentType : getPaintballEquipmentTypes()) {
            equipmentList.add(new Equipment(equipmentType.getName(), true, false, null, equipmentType));
        }
        return equipmentList;
    }

    public List<Equipment> getClimbingEquipmentTypeList() {

        List<Equipment> equipmentList = new ArrayList<>();
        for (EquipmentType equipmentType : getClimbingEquipmentTypes()) {
            equipmentList.add(new Equipment(equipmentType.getName(), true, false, null, equipmentType));
        }
        return equipmentList;
    }

    public List<Equipment> getGoKartEquipmentTypeList() {

        List<Equipment> equipmentList = new ArrayList<>();
        for (EquipmentType equipmentType : getGoKartEquipmentTypes()) {
            equipmentList.add(new Equipment(equipmentType.getName(), true, false, null, equipmentType));
        }
        return equipmentList;
    }


}