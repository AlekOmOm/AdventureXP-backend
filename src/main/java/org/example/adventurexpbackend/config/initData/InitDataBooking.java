package org.example.adventurexpbackend.config.initData;

import org.example.adventurexpbackend.model.Activity;
import org.example.adventurexpbackend.model.Booking;
import org.example.adventurexpbackend.repository.ActivityRepository;
import org.example.adventurexpbackend.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class InitDataBooking implements InitDataClass {

    private final ActivityRepository activityRepository;
    private List<Activity> activities;
    private final BookingRepository bookingRepository;

    @Autowired
    public InitDataBooking(BookingRepository bookingRepository, ActivityRepository activityRepository) {
        this.bookingRepository = bookingRepository;
        this.activityRepository = activityRepository;
    }

    @Override
    public void saveData() {
        this.activities = activityRepository.findAll();
        bookingRepository.saveAll(getBookings());
    }

    private List<Booking> getBookings() {
        return new ArrayList<>(List.of(
            new Booking("John the Baptist", LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(12, 0), activities.getFirst().getPersonsMax(), activities.get(0)), // Paintball
            new Booking("Francis of Assisi ", LocalDate.now(), LocalTime.of(12, 0), LocalTime.of(14, 0), 6, activities.get(0)), // Paintball 2 booking
            new Booking("Soeren Pind", LocalDate.now(), LocalTime.of(14, 0), LocalTime.of(16, 0), 8, activities.get(0)), // Paintball 3 booking
            new Booking("Scooby-Doo", LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(12, 0), activities.get(1).getPersonsMax(), activities.get(1)), // Climbing
            new Booking("Mickey Mouse", LocalDate.now(), LocalTime.of(12, 0), LocalTime.of(14, 0), 6, activities.get(1)), // Climbing 2 booking
            new Booking("John Lennon", LocalDate.now(), LocalTime.of(14, 0), LocalTime.of(16, 0), 8, activities.get(1)), // Climbing 3 booking
            new Booking("George Harrison", LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(12, 0), activities.get(2).getPersonsMax(), activities.get(2)), // Go-kart
            new Booking("Margrethe den Foerste", LocalDate.now(), LocalTime.of(12, 0), LocalTime.of(14, 0), 6, activities.get(2)), // Go-kart 2 booking
            new Booking("Churchill", LocalDate.now(), LocalTime.of(14, 0), LocalTime.of(16, 0), 8, activities.get(2)) // Go-kart 3 booking
        ));
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    };


}
