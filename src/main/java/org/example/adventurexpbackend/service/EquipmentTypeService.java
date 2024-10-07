package org.example.adventurexpbackend.service;

import org.example.adventurexpbackend.model.EquipmentType;
import org.example.adventurexpbackend.repository.EquipmentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EquipmentTypeService {

    private final EquipmentTypeRepository equipmentTypeRepository;

    @Autowired
    public EquipmentTypeService(EquipmentTypeRepository equipmentTypeRepository) {
        this.equipmentTypeRepository = equipmentTypeRepository;
    }

    // CRUD operations

    public EquipmentType saveEquipmentType(EquipmentType equipmentType) {
        return equipmentTypeRepository.save(equipmentType);
    }

    public EquipmentType getEquipmentType(Long id) {
        return equipmentTypeRepository.findById(id).orElse(null);
    }

    public void deleteEquipmentType(Long id) {
        equipmentTypeRepository.deleteById(id);
    }

    public EquipmentType updateEquipmentType(EquipmentType equipmentType) {
        return equipmentTypeRepository.save(equipmentType);
    }

}
