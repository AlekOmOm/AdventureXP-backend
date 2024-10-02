package org.example.adventurexpbackend.repository;

import org.example.adventurexpbackend.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

}
