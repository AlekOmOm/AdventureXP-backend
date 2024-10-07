package org.example.adventurexpbackend.repository;

import org.example.adventurexpbackend.model.Activity;
import org.example.adventurexpbackend.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByActivity(Activity activity);

    List<Booking> findByDate(LocalDate date);

    @Query("SELECT b FROM Booking b JOIN FETCH b.activity a LEFT JOIN FETCH a.equipmentList")
    List<Booking> findAllWithActivities();
}