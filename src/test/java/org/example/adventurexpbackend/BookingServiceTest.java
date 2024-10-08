package org.example.adventurexpbackend;

import org.example.adventurexpbackend.config.InitData;
import org.example.adventurexpbackend.dto.AvailableTimeSlot;
import org.example.adventurexpbackend.model.Activity;
import org.example.adventurexpbackend.model.Booking;
import org.example.adventurexpbackend.repository.ActivityRepository;
import org.example.adventurexpbackend.repository.BookingRepository;
import org.example.adventurexpbackend.repository.EquipmentRepository;
import org.example.adventurexpbackend.service.ActivityService;
import org.example.adventurexpbackend.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private InitData initData;

    @InjectMocks
    private ActivityService activityService;

    @InjectMocks
    private BookingService bookingService;

    private List<Activity> activities;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        activities = new ArrayList<>();
        when(activityRepository.findAll()).thenReturn(activities);
        activities = activityService.getAllActivities();
        System.out.println("Debug: Activities:");
        System.out.println(" " + activities);
    }

    // test for delete Activity if the booking is also deleted
    @Test
    public void testDeleteActivity() {
        Activity activity = new Activity();
        activity.setId(1L);
        activity.setName("Test Activity");
        activity.setPersonsMin(1);
        activity.setPersonsMax(10);
        activity.setOpeningTime(LocalTime.of(9, 0));
        activity.setClosingTime(LocalTime.of(17, 0));
        activity.setTimeSlotInterval(60);

        Booking booking = new Booking();
        booking.setActivity(activity);
        booking.setPersonsAmount(5);

        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);

        when(bookingRepository.findByActivity(activity)).thenReturn(bookings);

        bookingService.deleteActivity(activity);

        verify(bookingRepository, times(1)).save(booking);
        verify(activityRepository, times(1)).delete(activity);
    }


    @Test
    public void testBook_SuccessfulBooking() {
        if (activities.isEmpty()) {
            fail("No activities available for testing");
        }
        Activity activity = activities.get(0); // Assume the first activity is used
        activity.setPersonsMax(10);

        Booking booking = new Booking();
        booking.setActivity(activity);
        booking.setPersonsAmount(5);

        when(bookingRepository.findByActivity(activity)).thenReturn(new ArrayList<>());

        Booking result = bookingService.book(booking);

        assertNotNull(result);
        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    public void testBook_MaxParticipantsExceeded() {
        if (activities.isEmpty()) {
            fail("No activities available for testing");
        }
        Activity activity = activities.get(0); // Assume the first activity is used
        activity.setPersonsMax(10);

        Booking existingBooking = new Booking();
        existingBooking.setActivity(activity);
        existingBooking.setPersonsAmount(6);

        Booking newBooking = new Booking();
        newBooking.setActivity(activity);
        newBooking.setPersonsAmount(5);

        List<Booking> currentBookings = new ArrayList<>();
        currentBookings.add(existingBooking);

        when(bookingRepository.findByActivity(activity)).thenReturn(currentBookings);

        Booking result = bookingService.book(newBooking);

        assertNull(result);
        verify(bookingRepository, never()).save(newBooking);
    }

    @Test
    public void testGetAvailableTimes_NoBookings() {
        if (activities.isEmpty()) {
            fail("No activities available for testing");
        }
        Activity activity = activities.get(0); // Assume the first activity is used
        activity.setOpeningTime(LocalTime.of(9, 0));
        activity.setClosingTime(LocalTime.of(17, 0));
        activity.setTimeSlotInterval(60);

        LocalDate date = LocalDate.now();

        when(bookingRepository.findByActivity(activity)).thenReturn(new ArrayList<>());

        List<AvailableTimeSlot> availableTimes = bookingService.getAvailableTimes(activity, date, 5);

        assertEquals(8, availableTimes.size());
    }

    @Test
    public void testGetAvailableTimes_WithBookings() {
        if (activities.isEmpty()) {
            fail("No activities available for testing");
        }
        Activity activity = activities.get(0); // Assume the first activity is used
        activity.setOpeningTime(LocalTime.of(9, 0));
        activity.setClosingTime(LocalTime.of(17, 0));
        activity.setTimeSlotInterval(60);

        LocalDate date = LocalDate.now();

        Booking booking = new Booking();
        booking.setActivity(activity);
        booking.setStartTime(LocalTime.of(10, 0));
        booking.setEndTime(LocalTime.of(11, 0));

        List<Booking> bookingsAtDate = new ArrayList<>();
        bookingsAtDate.add(booking);

        when(bookingRepository.findByActivity(activity)).thenReturn(bookingsAtDate);

        List<AvailableTimeSlot> availableTimes = bookingService.getAvailableTimes(activity, date, 5);

        assertEquals(7, availableTimes.size());
    }
}