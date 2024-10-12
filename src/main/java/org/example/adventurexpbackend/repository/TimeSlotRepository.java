package org.example.adventurexpbackend.repository;

import org.example.adventurexpbackend.model.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
}
