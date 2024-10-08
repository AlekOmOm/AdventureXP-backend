package org.example.adventurexpbackend;

import org.example.adventurexpbackend.model.Activity;
import org.example.adventurexpbackend.model.Booking;
import org.example.adventurexpbackend.model.Equipment;
import org.example.adventurexpbackend.repository.ActivityRepository;
import org.example.adventurexpbackend.repository.BookingRepository;
import org.example.adventurexpbackend.repository.EquipmentRepository;
import org.example.adventurexpbackend.service.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RepositoryTest {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;
    @Autowired
    private BookingService bookingService;

    // test delete booking, save new booking, and test id is correctly set to highest id + 1
    @Test
    @Transactional
    void sequenceResetter() {
        List<Booking> bookings = bookingService.getAllBookings();
        Long lastIdBefore = bookings.getLast().getId();

        bookingService.deleteBooking(bookings.get(bookings.size()/2).getId());


        Booking booking = bookings.get(bookings.size()/2);
        booking.setId(null);
        booking.setPersonsAmount(1);
        bookingService.createBooking(booking);

        List<Booking> bookingsAfterSave = bookingService.getAllBookings();
        Long lastIdAfter = bookingsAfterSave.getLast().getId();

        assertEquals(lastIdBefore, lastIdAfter);
        System.out.println("Last id before: " + lastIdBefore);
        System.out.println("Last id after: " + lastIdAfter);
    }

    @Test
    void findAllActivities_ReturnsNonEmptyList() {
        List<Activity> activities = activityRepository.findAll();
        assertFalse(activities.isEmpty());

    }

    @Test
    void findActivityById_ReturnsCorrectActivity() {
        List<Activity> activities = activityRepository.findAll();
        Long id = activities.getFirst().getId();
        Activity activity = activityRepository.findById(id).orElse(null);
        assertNotNull(activity);

    }

    @Test
    void findActivityById_ReturnsNullForNonExistentId() {
        Long id = 999L; // Assuming no activity with ID 999 exists
        var activity = activityRepository.findById(id).orElse(null);
        assertNull(activity);

    }

    @Test
    void findAllBookings_ReturnsNonEmptyList() {
        List<Booking> bookings = bookingService.getAllBookings();
        assertFalse(bookings.isEmpty());

    }

    @Test
    void findBookingById_ReturnsCorrectBooking() {
        List<Booking> bookings = bookingService.getAllBookings();

        Long id = bookings.getFirst().getId();
        var booking = bookingRepository.findById(id).orElse(null);
        assertNotNull(booking);

    }

    @Test
    void findBookingById_ReturnsNullForNonExistentId() {
        Long id = 999L; // Assuming no booking with ID 999 exists
        var booking = bookingRepository.findById(id).orElse(null);
        assertNull(booking);
        System.out.println("Fetched booking: " + booking);
    }

    @Test
    void findAllEquipment_ReturnsNonEmptyList() {
        var equipments = equipmentRepository.findAll();
        assertFalse(equipments.isEmpty());
        equipments.forEach(equipment -> System.out.println("Fetched equipment: " + equipment));
    }

    @Test
    void findEquipmentById_ReturnsCorrectEquipment() {
        Long id = 1L; // Assuming equipment with ID 11 exists
        var equipment = equipmentRepository.findById(id).orElse(null);
        List<Equipment> equipments = equipmentRepository.findAll();
        assertNotNull(equipment);
        System.out.println("Fetched equipment: " + equipment);
        System.out.println("Fetched equipments: " + equipments);
    }

    @Test
    void findEquipmentById_ReturnsNullForNonExistentId() {
        Long id = 999L; // Assuming no equipment with ID 999 exists
        var equipment = equipmentRepository.findById(id).orElse(null);
        assertNull(equipment);
        System.out.println("Fetched equipment: " + equipment);
    }
}

