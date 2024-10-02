package org.example.adventurexpbackend.controller;


import org.example.adventurexpbackend.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EquipmentController {

    @Autowired
    EquipmentService equipmentService;

//---------------------------------------------------------------------------------------------------------------------
    // this is the endpoint to mark equipment as functional by the equipment id
    @PutMapping("/markAsFunctional/{id}")
    public ResponseEntity<String> markAsFunctional(@PathVariable long id) {
        String function = equipmentService.markAsFunctional(id);

        if (function.contains("error")){
            return ResponseEntity.badRequest().body(function);
        }
        return ResponseEntity.ok(function);
    }
//---------------------------------------------------------------------------------------------------------------------

}
