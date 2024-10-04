package org.example.adventurexpbackend.repository;

import org.example.adventurexpbackend.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    // Custom query to find activity by name
    Activity findByName(String name);
}

