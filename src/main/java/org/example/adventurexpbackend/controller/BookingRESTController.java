package org.example.adventurexpbackend.controller;

import org.example.adventurexpbackend.model.Booking;
import org.example.adventurexpbackend.repository.BookingRepository;
import org.example.adventurexpbackend.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/booking")
public class BookingRESTController {

    @Autowired
    private BookingService bookingService;


    //ResponseEntities need to be changed at somepoint to display custom messages, or be deleted if they are not needed

    @PostMapping
    public ResponseEntity<String> createBooking(@RequestBody Booking booking) {
        System.out.println("Received booking request:" + booking);

        boolean isBookingCreated = bookingService.createBooking(booking);

        if (isBookingCreated) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Booking successful");//custom message for a successful booking
        }else {
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


}
