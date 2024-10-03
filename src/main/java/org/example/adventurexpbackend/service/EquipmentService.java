package org.example.adventurexpbackend.service;

import org.example.adventurexpbackend.exceptions.ResourceNotFoundException;
import org.example.adventurexpbackend.model.Equipment;
import org.example.adventurexpbackend.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;



    //Method to mark an equipment as funtional
    public String markAsFunctional(Long equipmentId) {
        try{
            // Here we get(fetch) the equipment by the id
            Equipment equipment = equipmentRepository.findById(equipmentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Equipment with ID " + equipmentId + " not found"));
            // Here we set the equipment as functional
            equipment.setFunctional(true);
            // Here we save the equipment
            equipmentRepository.save(equipment);
            // Here we return a message
            return "Equipment marked as functional successfully.";
        }
        catch (ResourceNotFoundException e) {
            return "error: " + e.getMessage();
        }
    }

    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();

    }


}
