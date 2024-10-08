package org.example.adventurexpbackend.service;


import org.example.adventurexpbackend.config.SequenceResetter;
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

    private final SequenceResetter sequenceResetter;

    @Autowired
    public BookingService(BookingRepository bookingRepository, ActivityService activityService, SequenceResetter sequenceResetter) {
    public BookingService(BookingRepository bookingRepository, ActivityService activityService, RestTemplate restTemplate) {
        this.bookingRepository = bookingRepository;
        this.activityService = activityService;
        this.sequenceResetter = sequenceResetter;
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
        List<AvailableTimeSlot> availableTimeSlots = getTimeSlots(activity.getOpeningTime(), activity.getClosingTime(), activity.getTimeSlotInterval(), activity.getPersonsMax(), personsAmount);

        for (Booking booking : bookingsAtDate) {
            List<AvailableTimeSlot> bookingTimeSlots = getTimeSlots(booking.getStartTime(), booking.getEndTime(), booking.getActivity().getTimeSlotInterval(), booking.getActivity().getPersonsMax(), booking.getPersonsAmount());

            for (AvailableTimeSlot bookingTimeSlot : bookingTimeSlots) {
                availableTimeSlots.removeIf(availableTimeSlot ->
                        availableTimeSlot.getStartTime().isAfter(bookingTimeSlot.getStartTime()) &&
                                availableTimeSlot.getEndTime().isBefore(bookingTimeSlot.getEndTime()) &&
                                    availableTimeSlot.getAvailableSeats() < bookingTimeSlot.getAvailableSeats()
                );
            }
        }

        return availableTimeSlots;
    }

    private static List<AvailableTimeSlot> getTimeSlots(LocalTime startTime, LocalTime endTime, int timeSlotInterval, int activityMaxParticipants, int bookingParticipants) {
        List<AvailableTimeSlot> timeSlots = new ArrayList<>();
        while (startTime.isBefore(endTime)) {
            LocalTime slotEndTime = startTime.plusMinutes(timeSlotInterval);
            timeSlots.add(new AvailableTimeSlot(startTime, slotEndTime, activityMaxParticipants, bookingParticipants));
            startTime = slotEndTime;
        }
        return timeSlots;
    }

    // ----------------- CRUD Operations ---------------------

    public boolean createBooking(Booking booking) {
        Activity activity = activityService.getActivity(booking.getActivity());

        if (activity == null) {
            System.out.println("DEBUG: BookingService.createBooking");
            System.out.println(" Activity not found");
            return false;
        }

        List<AvailableTimeSlot> availableTimeSlots = getAvailableTimes(activity, booking.getDate(), booking.getPersonsAmount());

        // if booking time is within available time slots
        for (AvailableTimeSlot availableTimeSlot : availableTimeSlots) {
            if (booking.getStartTime().isAfter(availableTimeSlot.getStartTime()) && booking.getEndTime().isBefore(availableTimeSlot.getEndTime())) {
                booking.setActivity(activity);

                List<Booking> repoList = getAllBookings();
                if (!repoList.isEmpty()) {
                    sequenceResetter.resetAutoIncrement("booking", repoList.getLast().getId() + 1);
                }

                bookingRepository.save(booking);
                return true;
            }
        }

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

    public void deleteActivity(Activity activity) {
        List<Booking> bookings = bookingRepository.findByActivity(activity);
        for (Booking booking : bookings) {
            booking.setActivity(null); // Set activity to null to keep the booking
            bookingRepository.save(booking);
        }
        activityService.delete(activity);
    }

    // ----------------- Helper Methods ---------------------
}