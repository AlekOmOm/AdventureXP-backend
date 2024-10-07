package org.example.adventurexpbackend.controller;

import org.example.adventurexpbackend.model.Equipment;
import org.example.adventurexpbackend.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment")
@CrossOrigin(origins = "*")
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;


    //get all equipment
    @GetMapping("/all")
    public ResponseEntity<List<Equipment>> getAllEquipment() {
        List<Equipment> equipmentList = equipmentService.getAllEquipment();
        return ResponseEntity.ok(equipmentList);
    }

    // put to mark a equipment as functional
    @PutMapping("/mark-functional/{id}")
    public ResponseEntity<String> markEquipmentAsFunctional(@PathVariable Long id) {
        String result = equipmentService.markAsFunctional(id);

        if (result.contains("Error")) {
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }
}
