package org.example.adventurexpbackend.repository;

import org.example.adventurexpbackend.model.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {



}
