package org.example.adventurexpbackend.service;

import org.example.adventurexpbackend.model.Equipment;
import org.example.adventurexpbackend.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;

    public EquipmentService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    // CRUD operations


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
