package org.example.adventurexpbackend.service;

import org.example.adventurexpbackend.model.Activity;
import org.example.adventurexpbackend.model.Booking;
import org.example.adventurexpbackend.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ActivityService activityService;

    @Autowired
    public BookingService(BookingRepository bookingRepository, ActivityService activityService) {
        this.bookingRepository = bookingRepository;
        this.activityService = activityService;
    }

    // ----------------- Operations ---------------------

    public Booking book(Activity activity, LocalDate date, LocalTime startTime, int personsAmount) {
        // steps:
        // 1. check if the activity is available at the given date and time
        // 1a.


    }

    public List<LocalTime[]> getAvailableTimes(Activity activity, LocalDate date, int personsAmount) {

        List<Booking> bookingsAtDate = getBookingsByDate(activity, date);

        List<LocalTime[]> availableTimeSlots = getTimeSlots(activity.getOpeningTime(), activity.getClosingTime(), activity.getTimeSlotInterval());

        for (Booking booking : bookingsAtDate) {

            List<LocalTime[]> bookingTimeSlots = getTimeSlots(booking.getStartTime(), booking.getEndTime(), booking.activity.getTimeSlotInterval());

            for (LocalTime[] bookingTimeSlot : bookingTimeSlots) {
                for (LocalTime[] availableTimeSlot : availableTimeSlots) {
                    if (availableTimeSlot[0].isAfter(bookingTimeSlot[0]) && availableTimeSlot[1].isBefore(bookingTimeSlot[1])) {
                        availableTimeSlots.remove(availableTimeSlot);
                    }
                }
            }
        }

        return availableTimeSlots;
    }



    // ----------------- CRUD Operations ---------------------

    private Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id).orElse(null);
    }

    public List<Booking> getBookingsByDate(Activity activity, LocalDate date) {
        return (List<Booking>) bookingRepository.findByDate(date);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking updateBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }


    // ----------------- Helper Methods ---------------------

    private static List<LocalTime[]> getTimeSlots(LocalTime startTime, LocalTime endTime, int timeSlotInterval) {
        List<LocalTime[]> timeSlots = new ArrayList<>();
        while (startTime.isBefore(endTime)) {
            LocalTime[] timeSlotArray = {startTime, startTime.plusMinutes(timeSlotInterval)};
            timeSlots.add(timeSlotArray);
            startTime = startTime.plusMinutes(timeSlotInterval);
        }
        return timeSlots;
    }


}
