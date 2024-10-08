package org.example.adventurexpbackend.controller;

import org.example.adventurexpbackend.model.TimeSlot;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TimeSlotRestController {
    @GetMapping("all/timeslots")
    public List<TimeSlot> getAllTimeSlots() {
        List<TimeSlot> timeSlots = getAllTimeSlots();
        return timeSlots;
    }
}
