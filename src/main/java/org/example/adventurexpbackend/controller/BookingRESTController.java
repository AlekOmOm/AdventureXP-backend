package org.example.adventurexpbackend.controller;

import org.example.adventurexpbackend.model.Activity;
import org.example.adventurexpbackend.model.Booking;
import org.example.adventurexpbackend.model.TimeSlot;
import org.example.adventurexpbackend.repository.BookingRepository;
import org.example.adventurexpbackend.service.ActivityService;
import org.example.adventurexpbackend.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/booking")
public class BookingRESTController {

    @Autowired
    private BookingService bookingService;
    @Autowired
    private ActivityService activityService;


    //ResponseEntities need to be changed at somepoint to display custom messages, or be deleted if they are not needed

    @PostMapping
    public ResponseEntity<String> createBooking(@RequestBody Booking booking) {
        System.out.println("Received booking request:" + booking);

        if (bookingService.book(booking) != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Booking successful");//custom message for a successful booking
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Booking denied: Max participants limit reached");// custom message for a denied
        }
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        Optional<Booking> booking = Optional.ofNullable(bookingService.getBookingById(id));
        return booking.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable Long id, @RequestBody Booking updatedBooking) {
        Booking booking = bookingService.updateBooking(updatedBooking);
        if (booking == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(booking);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Booking> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.ok().build();
    }


    //-------------------------------------------------------------------------------------
    // Endpoint to get all available timeslots for a specific activity and deto

    @GetMapping("/available-timeslots")
    public ResponseEntity<List<TimeSlot>> getAvailableTimeSlots(@RequestParam Long activityId, @RequestParam String date, @RequestParam int personsAmount) {

        Optional<Activity> activityOptional = activityService.getActivityById(activityId);  // No need for Optional.ofNullable()
        if (activityOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        LocalDate bookingDate;
        try {
            bookingDate = LocalDate.parse(date);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<TimeSlot> availableTimeSlots = bookingService.getAvailableTimes(activityOptional.get(), bookingDate, personsAmount);

        return availableTimeSlots.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(availableTimeSlots);
    }

    //----------------------------------------------------------------------------------------------------------------------
// Endpoint to book a specific timeslot for an activity gry
    @PostMapping("/book-timeslot")
    public ResponseEntity<String> bookTimeSlot(@RequestParam Long activityId, @RequestParam Long timeSlotId, @RequestParam String participantName, @RequestParam int personsAmount) {

        Optional<Activity> activityOptional = activityService.getActivityById(activityId);  // No need for Optional.ofNullable()
        if (activityOptional.isEmpty()) {
            return new ResponseEntity<>("Activity not found", HttpStatus.NOT_FOUND);
        }

        Activity activity = activityOptional.get();
        Optional<TimeSlot> timeSlotOptional = activity.getTimeSlots().stream()
                .filter(ts -> ts.getId().equals(timeSlotId))
                .findFirst();

        if (timeSlotOptional.isEmpty()) {
            return new ResponseEntity<>("Timeslot not found", HttpStatus.NOT_FOUND);
        }

        TimeSlot timeSlot = timeSlotOptional.get();
        if (!timeSlot.isAvailable()) {
            return new ResponseEntity<>("Timeslot is already booked", HttpStatus.BAD_REQUEST);
        }

        Booking booking = new Booking();
        booking.setActivity(activity);
        booking.setDate(timeSlot.getDate());
        booking.setStartTime(timeSlot.getStartTime());
        booking.setEndTime(timeSlot.getEndTime());
        booking.setPersonsAmount(personsAmount);
        booking.setParticipantName(participantName);

        Booking createdBooking = bookingService.createBooking(booking);

        if (createdBooking == null) {
            return new ResponseEntity<>("Booking failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        timeSlot.setAvailable(false);
        activityService.saveActivity(activity);

        return ResponseEntity.status(HttpStatus.CREATED).body("Timeslot booked successfully");
    }
}

