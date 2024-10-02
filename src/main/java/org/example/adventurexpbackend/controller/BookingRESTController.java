package org.example.adventurexpbackend.controller;

import org.example.adventurexpbackend.model.Booking;
import org.example.adventurexpbackend.repository.BookingRepository;
import org.example.adventurexpbackend.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class BookingRESTController {

    @Autowired
    private BookingRepository bookingRepository;


    public List<Booking> getAllBookings(){
        return bookingRepository.findAll();
    }

    public Optional<Booking> getBookingById(Long id){
        return bookingRepository.findAllById(id);
    }

    public void deleteBookingById(Long id){

    }
}
