package org.example.adventurexpbackend.repository;

import org.example.adventurexpbackend.model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

}
