package org.example.adventurexpbackend.service;

import org.example.adventurexpbackend.model.Equipment;
import org.example.adventurexpbackend.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    // ----------------- Operations ---------------------


    public Equipment save(Equipment equipment) {
        return equipmentRepository.save(equipment);
    }

    public Iterable<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
    }

    public Equipment get(Equipment equipment) {
        if(equipment.getId() != null) {
            return equipmentRepository.findById(equipment.getId()).orElse(null);
        } else {
            return equipmentRepository.findByName(equipment.getName());
        }
    }

    public Optional<Equipment> getEquipmentById(Long id) {
        return equipmentRepository.findById(id);
    }

    public Optional<Equipment> getEquipmentByName(String name) {
        return Optional.ofNullable(equipmentRepository.findByName(name));
    }

    // Delete equipment by id
    public void deleteEquipmentById(Long id) {
        equipmentRepository.deleteById(id);
    }

}
