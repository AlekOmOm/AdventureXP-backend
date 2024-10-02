package org.example.adventurexpbackend.service;

import org.example.adventurexpbackend.exceptions.ResourceNotFoundException;
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;

public class EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;



    //Method to mark an equipment as funtional
    public void markAsFunctional(Long equipmentId) {

        try{
            Equipment equipment = equipmentRepository.findById)
            .orElseTrow(() -> ResourceNotFoundException("Suffer! there is no equipment with" + equipmentId + " found"));

            equipment.setFunctional(true);
            equipmentRepository.save(equipment);
        }

        catch (ResourceNotFoundException e){
            System.out.println("Fail in method MarkAsFunctional");
        }




    }



}
