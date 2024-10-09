package org.example.adventurexpbackend.service;

import org.example.adventurexpbackend.model.Equipment;
import org.example.adventurexpbackend.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public EquipmentService(EquipmentRepository equipmentRepository, RestTemplate restTemplate) {
        this.equipmentRepository = equipmentRepository;
        this.restTemplate = restTemplate;
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

    public Equipment save(Equipment equipment) {
        return equipmentRepository.save(equipment);
    }

    // Retrieve all equipment
    public Iterable<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
    }

    // Retrieve equipment by id
    public Optional<Equipment> getEquipmentById(Long id) {
        return equipmentRepository.findById(id);
    }

    // Retrieve equipment by name
    public Optional<Equipment> getEquipmentByName(String name) {
        return Optional.ofNullable(equipmentRepository.findByName(name));
    }

    // Delete equipment by id
    public void deleteEquipmentById(Long id) {
        equipmentRepository.deleteById(id);
    }

}
