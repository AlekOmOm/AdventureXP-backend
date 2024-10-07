package org.example.adventurexpbackend.repository;

import org.example.adventurexpbackend.model.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    @Override
    List<Activity> findAll();
    // Custom query to find activity by name
    Activity findByName(String name);
}

