package org.example.adventurexpbackend.config.initData;

import org.example.adventurexpbackend.model.Activity;
import org.example.adventurexpbackend.model.Booking;
import org.example.adventurexpbackend.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class InitDataBooking implements InitDataClass {

    private List<Activity> activities;


    private final BookingRepository bookingRepository;

    @Autowired
    public InitDataBooking(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }


    @Override
    public void saveData() {
        bookingRepository.saveAll(getBookings());
    }

    private List<Booking> getBookings() {
        return new ArrayList<>(List.of(
            new Booking(LocalTime.of(10, 0), LocalTime.of(12, 0), activities.getFirst().getPersonsMax(), activities.get(0)), // Paintball
            new Booking(LocalTime.of(12, 0), LocalTime.of(14, 0), 6, activities.get(0)), // Paintball 2 booking
            new Booking(LocalTime.of(14, 0), LocalTime.of(16, 0), 8, activities.get(0)), // Paintball 3 booking
            new Booking(LocalTime.of(10, 0), LocalTime.of(12, 0), activities.get(1).getPersonsMax(), activities.get(1)), // Climbing
            new Booking(LocalTime.of(12, 0), LocalTime.of(14, 0), 6, activities.get(1)), // Climbing 2 booking
            new Booking(LocalTime.of(14, 0), LocalTime.of(16, 0), 8, activities.get(1)), // Climbing 3 booking
            new Booking(LocalTime.of(10, 0), LocalTime.of(12, 0), activities.get(2).getPersonsMax(), activities.get(2)), // Go-kart
            new Booking(LocalTime.of(12, 0), LocalTime.of(14, 0), 6, activities.get(2)), // Go-kart 2 booking
            new Booking(LocalTime.of(14, 0), LocalTime.of(16, 0), 8, activities.get(2)) // Go-kart 3 booking
        ));
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    };


}
