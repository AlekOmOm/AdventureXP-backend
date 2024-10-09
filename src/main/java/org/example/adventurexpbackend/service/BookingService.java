package org.example.adventurexpbackend.service;

import org.example.adventurexpbackend.model.Activity;
import org.example.adventurexpbackend.model.Booking;
import org.example.adventurexpbackend.model.TimeSlot;
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

    public Booking book(Booking booking) {
        boolean isBookingCreated = createBooking(booking);

        if (isBookingCreated) {
            return booking;
        } else {
            return null; // return null if booking was denied
        }
    }

    // Method to get available TimeSlots for a specific activity and date
    public List<TimeSlot> getAvailableTimes(Activity activity, LocalDate date, int personsAmount) {
        // Fetch all timeslots for the activity
        List<TimeSlot> availableTimeSlots = new ArrayList<>(activity.getTimeSlots());

        // Fetch bookings for the given date and filter out timeslots that are already booked
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

    public boolean createBooking(Booking booking) {
        Activity activity = activityService.getActivity(booking.getActivity());

        if (activity == null) {
            System.out.println("DEBUG: BookingService.createBooking - Activity not found");
            return false;
        }

        // Check if the requested timeslot is available
        List<TimeSlot> availableTimeSlots = getAvailableTimes(activity, booking.getDate(), booking.getPersonsAmount());

        for (TimeSlot availableTimeSlot : availableTimeSlots) {
            if (booking.getStartTime().isAfter(availableTimeSlot.getStartTime()) && booking.getEndTime().isBefore(availableTimeSlot.getEndTime())) {

                // Reserve the timeslot
                booking.setActivity(activity);

                // Save booking
                bookingRepository.save(booking);

                // Update TimeSlot
                updateTimeSlotAvailability(activity, booking.getStartTime(), booking.getEndTime());

                return true;
            }
        }

        return false;//if no free timeslot
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

    // Update the availability of a TimeSlot when booked
    private void updateTimeSlotAvailability(Activity activity, LocalTime bookingStartTime, LocalTime bookingEndTime) {

        for (TimeSlot timeSlot : activity.getTimeSlots()) {
            if (timeSlot.getStartTime().equals(bookingStartTime) && timeSlot.getEndTime().equals(bookingEndTime)) {

                timeSlot.setAvailable(false);
                break;
            }
        }
        activityService.saveActivity(activity); // Save the updated activity
    }
}
