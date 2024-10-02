package org.example.adventurexpbackend.config.initData;

import org.example.adventurexpbackend.model.Booking;
import org.example.adventurexpbackend.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class InitDataBooking implements InitDataClass {

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
            new Booking("2021-12-24", "10:00", "12:00", 1, 1),
            new Booking("2021-12-24", "12:00", "14:00", 1, 1),
            new Booking("2021-12-24", "14:00", "16:00", 1, 1),
            new Booking("2021-12-24", "16:00", "18:00", 1, 1),
            new Booking("2021-12-24", "18:00", "20:00", 1, 1),
            new Booking("2021-12-24", "20:00", "22:00", 1, 1),
            new Booking("2021-12-24", "22:00", "00:00", 1, 1),
            new Booking("2021-12-24", "00:00", "02:00", 1, 1),
            new Booking("2021-12-24", "02:00", "04:00", 1, 1),
            new Booking("2021-12-24", "04:00", "06:00", 1, 1),
            new Booking("2021-12-24", "06:00", "08:00", 1, 1)
        ));
    }


}
