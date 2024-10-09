package org.example.adventurexpbackend.controller.dto;

import java.time.LocalTime;

public class AvailableTimeSlot {
    private LocalTime startTime;
    private LocalTime endTime;
    // activity max participants and booking participants = available seats
    private int availableSeats;

    public AvailableTimeSlot(LocalTime startTime, LocalTime endTime, int activityMaxParticipants, int bookingParticipants) {
        this.startTime = startTime;
        this.endTime = endTime;
        setAvailableSeats(activityMaxParticipants, bookingParticipants);
    }



    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    private void setAvailableSeats(int activityMaxParticipants, int bookingParticipants) {
        this.availableSeats = activityMaxParticipants - bookingParticipants;
    }
}