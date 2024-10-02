package org.example.adventurexpbackend.config.initData;

import org.example.adventurexpbackend.model.Activity;
import org.example.adventurexpbackend.model.Equipment;
import org.example.adventurexpbackend.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class InitDataEquipment {
    private List<Activity> activities;
    private final EquipmentRepository equipmentRepository;

    @Autowired
    public InitDataEquipment(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    public void saveData(List<Activity> activities) {
        this.activities = activities;
        equipmentRepository.saveAll(getEquipment());
    }

    // Equipment is for the activities: Paintball, Climbing, Go-kart
    private List<Equipment> getEquipment() {
        return new ArrayList<>(List.of(
                new Equipment("Paintball gun", true, false, activities.get(0)),
                new Equipment("Paintball", "Paintball mask", 10, 10),
                new Equipment("Paintball", "Paintball suit", 10, 10),
                new Equipment("Climbing", "Climbing shoes", 10, 10),
                new Equipment("Climbing", "Climbing harness", 10, 10),
                new Equipment("Climbing", "Climbing rope", 10, 10),
                new Equipment("Go-kart", "Go-kart helmet", 10, 10),
                new Equipment("Go-kart", "Go-kart suit", 10, 10),
                new Equipment("Go-kart", "Go-kart gloves", 10, 10)
        ));
    }

    private
}
