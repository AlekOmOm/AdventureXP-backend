package org.example.adventurexpbackend.service;

import org.example.adventurexpbackend.model.Equipment;
import org.example.adventurexpbackend.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;


    public EquipmentService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }


    // Method to get all equipments
    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
    }


    // Method to mark equipment as functionall
    public String markAsFunctional(Long equipmentId) {
        Optional<Equipment> optionalEquipment = equipmentRepository.findById(equipmentId);
        // CRUD operations

        if (optionalEquipment.isPresent()) {
            Equipment equipment = optionalEquipment.get();
            equipment.setFunctional(true);
            equipmentRepository.save(equipment);
            return "Equipment marked as functional.";
        } else {
            return "Error: Equipment with ID " + equipmentId + " not found.";
        }


    }

    public Equipment save (Equipment equipment){
        return equipmentRepository.save(equipment);
    }
}
