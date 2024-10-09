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
        boolean isBookingCreated = bookingService.createBooking(booking);

        if (isBookingCreated) {
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
    public ResponseEntity<List<TimeSlot>> getAvailableTimeSlots(
            @RequestParam Long activityId,
            @RequestParam String date,
            @RequestParam int personsAmount) {

        Optional<Activity> activityOptional = Optional.ofNullable(activityService.getActivityById(activityId));
        if (activityOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Activity activity = activityOptional.get();

        LocalDate bookingDate;
        try {
            bookingDate = LocalDate.parse(date);}
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);}
        List<TimeSlot> availableTimeSlots = bookingService.getAvailableTimes(activity, bookingDate, personsAmount);
        if (!availableTimeSlots.isEmpty()) {return new ResponseEntity<>(availableTimeSlots, HttpStatus.OK);
        } else {return new ResponseEntity<>(HttpStatus.NO_CONTENT);}
    }
}
