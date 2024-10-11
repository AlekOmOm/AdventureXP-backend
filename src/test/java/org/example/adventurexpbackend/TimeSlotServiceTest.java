package org.example.adventurexpbackend;

import org.example.adventurexpbackend.model.Activity;
import org.example.adventurexpbackend.model.TimeSlot;
import org.example.adventurexpbackend.model.Booking;
import org.example.adventurexpbackend.repository.ActivityRepository;
import org.example.adventurexpbackend.repository.BookingRepository;
import org.example.adventurexpbackend.service.TimeslotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TimeSlotServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private TimeslotService timeSlotService;

    private Activity paintballActivity;
    private TimeSlot availableTimeSlot;
    private Booking booking;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create a sample Activity (Paintball)
        paintballActivity = new Activity();
        paintballActivity.setId(1L);
        paintballActivity.setName("Paintball");

        // Create a sample TimeSlot (from 10:00 to 11:00)
        availableTimeSlot = new TimeSlot();
        availableTimeSlot.setId(1L);
        availableTimeSlot.setStartTime(LocalTime.of(10, 0));
        availableTimeSlot.setEndTime(LocalTime.of(11, 0));
        availableTimeSlot.setMaxParticipants(10);
        availableTimeSlot.setCurrentParticipants(0);
        availableTimeSlot.setAvailable(true);

        // Add the time slot to the activity
        List<TimeSlot> timeSlots = new ArrayList<>();
        timeSlots.add(availableTimeSlot);
        paintballActivity.setTimeSlots(timeSlots);

        // Create a sample Booking
        booking = new Booking();
        booking.setParticipantName("John Doe");
        booking.setPersonsAmount(5);
    }

    @Test
    public void testBookTimeSlot_Success() {
        // Mock the behavior of the repositories
        when(activityRepository.findById(1L)).thenReturn(Optional.of(paintballActivity));

        // Call the service method
        String result = timeSlotService.bookTimeSlot(1L, 1L, booking);

        // Verify the interaction with the repositories
        verify(activityRepository, times(1)).save(paintballActivity);
        verify(bookingRepository, times(1)).save(booking);

        // Assert the expected result
        assertEquals("Timeslot booked", result);
        assertEquals(false, availableTimeSlot.isAvailable()); // TimeSlot should be marked as unavailable
    }

    @Test
    public void testBookTimeSlot_TimeSlotAlreadyBooked() {
        // Mark the timeslot as already booked
        availableTimeSlot.setAvailable(false);

        // Mock the behavior of the repositories
        when(activityRepository.findById(1L)).thenReturn(Optional.of(paintballActivity));

        // Call the service method
        String result = timeSlotService.bookTimeSlot(1L, 1L, booking);

        // Assert that the booking failed because the timeslot is already booked
        assertEquals("Timeslot is not free", result);
        verify(activityRepository, never()).save(any(Activity.class));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    public void testBookTimeSlot_ActivityNotFound() {
        // Mock the activity not being found in the repository
        when(activityRepository.findById(1L)).thenReturn(Optional.empty());

        // Call the service method
        String result = timeSlotService.bookTimeSlot(1L, 1L, booking);

        // Assert that the booking failed because the activity was not found
        assertEquals("Activity were not found.", result);
        verify(activityRepository, never()).save(any(Activity.class));
        verify(bookingRepository, never()).save(any(Booking.class));
    }
}
