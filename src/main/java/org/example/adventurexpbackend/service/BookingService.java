package org.example.adventurexpbackend.service;


import org.example.adventurexpbackend.config.SequenceResetter;
import org.example.adventurexpbackend.model.Activity;
import org.example.adventurexpbackend.model.Booking;
import org.example.adventurexpbackend.model.TimeSlot;
import org.example.adventurexpbackend.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.example.adventurexpbackend.controller.dto.AvailableTimeSlot;
import org.springframework.web.client.RestTemplate;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ActivityService activityService;
    private final RestTemplate restTemplate;
    private final SequenceResetter sequenceResetter;

    @Autowired
    public BookingService(BookingRepository bookingRepository, ActivityService activityService, SequenceResetter sequenceResetter, RestTemplate restTemplate) {
        this.bookingRepository = bookingRepository;
        this.activityService = activityService;
        this.sequenceResetter = sequenceResetter;
        this.restTemplate = restTemplate;
    }

    // ----------------- Operations ---------------------
    public Booking book(Booking booking) {
        try {
            return createBooking(booking);
        } catch (Exception e) {
            return null;
        }
    }

    public List<TimeSlot> getAvailableTimes(Activity activity, LocalDate date, int personsAmount) {
        // Get all timeslots for the activity
        List<TimeSlot> availableTimeSlots = new ArrayList<>(activity.getTimeSlots());

        // Fetch and filter so only available timeslots are returned
        List<Booking> bookingsAtDate = getBookingsByDate(activity, date);

        for (Booking booking : bookingsAtDate) {
            availableTimeSlots.removeIf(timeSlot ->
                    timeSlot.getStartTime().isBefore(booking.getEndTime()) &&
                            timeSlot.getEndTime().isAfter(booking.getStartTime()) &&
                            timeSlot.getMaxParticipants() < booking.getPersonsAmount()
            );
        }

        return availableTimeSlots; // Return the list of available timeslots
    }

    // ----------------- CRUD Operations ---------------------
    @Transactional
    public Booking createBooking(Booking booking) {
        Activity activity = activityService.getActivity(booking.getActivity());

        // Check if activity exists
        if (activity == null) {
            System.out.println("DEBUG: BookingService.createBooking - Activity not found");
            return null;
        }

        // Capacity check: Ensure personsAmount does not exceed activity limit
        if (booking.getPersonsAmount() > activity.getPersonsMax()) {
            System.out.println("DEBUG: BookingService.createBooking - Persons limit exceeded");
            return null;
        }

        List<TimeSlot> availableTimeSlots = getAvailableTimes(activity, booking.getDate(), booking.getPersonsAmount());

        for (TimeSlot availableTimeSlot : availableTimeSlots) {
            if (booking.getStartTime().isAfter(availableTimeSlot.getStartTime()) && booking.getEndTime().isBefore(availableTimeSlot.getEndTime())) {
                booking.setActivity(activity);

                // Save booking
                bookingRepository.save(booking);

                // Update TimeSlot
                updateTimeSlotAvailability(activity, booking.getStartTime(), booking.getEndTime());

                return booking;  // Return the created booking if successful
            }
        }

        System.out.println("DEBUG: No available timeslot found for the booking.");
        return null;  // Return null if no available timeslot was found
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

    public void deleteActivity(Activity activity) {
        List<Booking> bookings = bookingRepository.findByActivity(activity);
        for (Booking booking : bookings) {
            booking.setActivity(null); // Set activity to null to keep the booking
            bookingRepository.save(booking);
        }
        activityService.delete(activity);
    }

    // ----------------- Helper Methods ---------------------
    // Update the availability of a TimeSlot when booked
    @Transactional
    public void updateTimeSlotAvailability(Activity activity, LocalTime bookingStartTime, LocalTime bookingEndTime) {
        for (TimeSlot timeSlot : activity.getTimeSlots()) {
            if (timeSlot.getStartTime().equals(bookingStartTime) && timeSlot.getEndTime().equals(bookingEndTime)) {
                timeSlot.setAvailable(false);
                break;
            }
        }
        activityService.saveActivity(activity);
    }
}