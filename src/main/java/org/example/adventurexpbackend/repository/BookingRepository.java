package org.example.adventurexpbackend.repository;

import org.example.adventurexpbackend.model.Activity;
import org.example.adventurexpbackend.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Iterable<Booking> findByDate(LocalDate date);

    List<Booking> findByActivity(Activity activity);
}
