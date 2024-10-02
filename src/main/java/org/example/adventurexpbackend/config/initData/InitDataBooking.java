package org.example.adventurexpbackend.config.initData;

public class InitDataBooking implements InitDataClass {

    private final BookingRepository bookingRepository;

    @Override
    public void saveData() {
        bookingRepository.save(new Booking("2021-12-24", "12:00", "14:00", 1, 1));
        bookingRepository.save(new Booking("2021-12-24", "14:00", "16:00", 1, 1));
        bookingRepository.save(new Booking("2021-12-24", "16:00", "18:00", 1, 1));
        bookingRepository.save(new Booking("2021-12-24", "18:00", "20:00", 1, 1));
        bookingRepository.save(new Booking("2021-12-24", "20:00", "22:00", 1, 1));
        bookingRepository.save(new Booking("2021-12-24", "22:00", "00:00", 1, 1));
        bookingRepository.save(new Booking("2021-12-24", "00:00", "02:00", 1, 1));
        bookingRepository.save(new Booking("2021-12-24", "02:00", "04:00", 1, 1));
        bookingRepository.save(new Booking("2021-12-24", "04:00", "06:00", 1, 1));
        bookingRepository.save(new Booking("2021-12-24", "06:00", "08:00", 1, 1));

    }
}
