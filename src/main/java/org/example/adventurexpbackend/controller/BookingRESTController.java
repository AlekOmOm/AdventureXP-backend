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
import org.springframework.web.server.ResponseStatusException;

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

    // ------------------- Operations -------------------

    // ------------------- 1. Create -------------------
    @PostMapping
    public ResponseEntity<String> createBooking(@RequestBody Booking booking, @RequestBody Activity activity) {
        System.out.println("Received booking request:" + booking);

        Booking bookingCreated = bookingService.createBooking(booking);

        if (bookingCreated != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Booking successful");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Booking denied: Max participants limit reached");
        }
    }

    // ------------------- 2. Read -------------------
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        return getBooking(bookingService.getBookingById(id));
    }

    // ------------------- 3. Update -------------------
    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable Long id, @RequestBody Booking updatedBooking) {
        return getBooking(bookingService.updateBooking(updatedBooking));
    }

    // ------------------- 4. Delete -------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Booking> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.ok().build();
    }


    // ------------------- TimeSlot Operations -------------------
    // Endpoint to get all available timeslots for a specific activity and dto
    @GetMapping("/available-timeslots")
    public ResponseEntity<List<TimeSlot>> getAvailableTimeSlots(
            @RequestParam Long activityId,
            @RequestParam String date,
            @RequestParam int personsAmount) {

        LocalDate bookingDate = parseDate(date);

        List<TimeSlot> availableTimeSlots = bookingService.getAvailableTimes(activity, bookingDate, personsAmount);

        return createResponseEntity(availableTimeSlots);
    }

    //----------------------------------------------------------------------------------------------------------------------
// Endpoint to book a specific timeslot for an activity gry
    @PostMapping("/book-timeslot")
    public ResponseEntity<String> bookTimeSlot(@RequestParam Long activityId, @RequestParam Long timeSlotId, @RequestParam String participantName, @RequestParam int personsAmount) {


        Optional<Activity> activityOptional = activityService.getActivity(activityId);
        if (activityOptional.isEmpty()) {
            return new ResponseEntity<>("Activity not found bruh", HttpStatus.NOT_FOUND);
        }

        Optional<TimeSlot> timeSlot = activityOptional.get().getTimeSlots().stream()
                .filter(ts -> ts.getId().equals(timeSlotId))
                .findFirst();

        if (timeSlot.isEmpty()) {
            return new ResponseEntity<>("Timeslot not found bruh", HttpStatus.NOT_FOUND);
        }

        timeSlot.get().addParticipants(personsAmount);

        activityOptional.get().updateTimeSlot(timeSlot.get());





        Booking bookingCreated = bookingService.createBooking(booking);

        if (bookingCreated == null) {
            return new ResponseEntity<>("Fail", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        timeSlot.get().addParticipants(personsAmount);
        activity.getTimeSlots().remove(timeSlot);

        activityService.saveActivity(activity);

        return ResponseEntity.status(HttpStatus.CREATED).body("woop woop Timeslot booked successfully woop woop");
    }


    // ------------------- Helper -------------------

    private ResponseEntity<Booking> getBooking(Booking booking) {
        return Optional.ofNullable(booking)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private boolean isBookingSuccessful(Booking booking) {
        return bookingService.createBooking(booking) != null;
    }


    private Optional<TimeSlot> getTimeSlotById(Activity activity, Long timeSlotId) {
        return activity.getTimeSlots().stream()
                .filter(ts -> ts.getId().equals(timeSlotId))
                .findFirst();
    }

    private void validateOptional(Optional<?> optional, String entityName) {
        if (optional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, entityName + " not found");
        }
    }

    private LocalDate parseDate(String date) {
        LocalDate result;
        try {
            result = LocalDate.parse(date);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date format", e);
        }
        return result;
    }

    private <T> ResponseEntity<List<T>> createResponseEntity(List<T> data) {
        return data.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(data, HttpStatus.OK);
    }
}

