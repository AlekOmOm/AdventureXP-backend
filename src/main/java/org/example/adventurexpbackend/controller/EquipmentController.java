package org.example.adventurexpbackend.controller;

import org.example.adventurexpbackend.model.Equipment;
import org.example.adventurexpbackend.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/equipment")
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    // Endpoint to create or update an equipment
    @PostMapping
    public ResponseEntity<Equipment>createOrUpdateEquipment(@RequestBody Equipment equipment){
        Equipment savedEquipment = equipmentService.save(equipment);
        return ResponseEntity.ok(savedEquipment);
    }

    // Endpoint to retrieve all equipment
    @GetMapping
    public ResponseEntity<List<Equipment>> getAllEquipment(){
        List<Equipment> equipment = (List<Equipment>) equipmentService.getAllEquipment();
        return ResponseEntity.ok(equipment);
    }

    // Endpoint to retrieve equipment by id
    @GetMapping("/{id}")
    public ResponseEntity<Equipment> getEquipmentById(@PathVariable Long id){
        Optional<Equipment> equipment = equipmentService.getEquipmentById(id);
        return equipment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint to retrieve equipment by name
    @GetMapping("/name/{name}")
    public ResponseEntity<Equipment> getEquipmentByName(@PathVariable String name){
        Optional<Equipment> equipment = equipmentService.getEquipmentByName(name);
        return equipment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint to delete equipment by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipmentById(@PathVariable Long id){
        equipmentService.deleteEquipmentById(id);
        return ResponseEntity.noContent().build();
    }

}
