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
import org.example.adventurexpbackend.dto.AvailableTimeSlot;
import org.springframework.web.client.RestTemplate;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ActivityService activityService;
    private final RestTemplate restTemplate;


    @Autowired
    public BookingService(BookingRepository bookingRepository, ActivityService activityService, RestTemplate restTemplate) {
        this.bookingRepository = bookingRepository;
        this.activityService = activityService;
        this.restTemplate = restTemplate;
    }

    // ----------------- Operations ---------------------

    public Booking book(Booking booking) {
        boolean isBookingCreated = createBooking(booking);

        if (isBookingCreated) {
            return booking;
        } else {
            return null; // return null if booking was denied
        }
    }

    public List<AvailableTimeSlot> getAvailableTimes(Activity activity, LocalDate date, int personsAmount) {
        List<Booking> bookingsAtDate = getBookingsByDate(activity, date);
        List<AvailableTimeSlot> availableTimeSlots = getTimeSlots(activity.getOpeningTime(), activity.getClosingTime(), activity.getTimeSlotInterval());

        for (Booking booking : bookingsAtDate) {
            List<AvailableTimeSlot> bookingTimeSlots = getTimeSlots(booking.getStartTime(), booking.getEndTime(), booking.getActivity().getTimeSlotInterval());

            for (AvailableTimeSlot bookingTimeSlot : bookingTimeSlots) {
                availableTimeSlots.removeIf(availableTimeSlot ->
                        availableTimeSlot.getStartTime().isAfter(bookingTimeSlot.getStartTime()) &&
                                availableTimeSlot.getEndTime().isBefore(bookingTimeSlot.getEndTime())
                );
            }
        }

        return availableTimeSlots;
    }

    private static List<AvailableTimeSlot> getTimeSlots(LocalTime startTime, LocalTime endTime, int timeSlotInterval) {
        List<AvailableTimeSlot> timeSlots = new ArrayList<>();
        while (startTime.isBefore(endTime)) {
            LocalTime slotEndTime = startTime.plusMinutes(timeSlotInterval);
            timeSlots.add(new AvailableTimeSlot(startTime, slotEndTime));
            startTime = slotEndTime;
        }
        return timeSlots;
    }

    // ----------------- CRUD Operations ---------------------

    public boolean createBooking(Booking booking) {
        Activity activity = booking.getActivity();
        int maxParticipants = activity.getPersonsMax();

        List<Booking> currentBookings = bookingRepository.findByActivity(activity);

        int totalCurrentParticipants = currentBookings.stream().mapToInt(Booking::getPersonsAmount).sum();

        if (totalCurrentParticipants + booking.getPersonsAmount() > maxParticipants) {
            return false;
        }

        bookingRepository.save(booking);
        return true;
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id).orElse(null);
    }

    public List<Booking> getBookingsByDate(Activity activity, LocalDate date) {
        return (List<Booking>) bookingRepository.findByDate(date);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAllWithActivities();
    }

    public Booking updateBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    // ----------------- Helper Methods ---------------------
}