package com.example.licentaRestaurante.controllers;

import com.example.licentaRestaurante.models.bookings.SingleBooking;
import com.example.licentaRestaurante.services.BookingService;
import com.example.licentaRestaurante.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/booking")
public class BookingController {
    private final BookingService bookingService;
    private final JwtUtils jwtUtils;

    @Autowired
    public BookingController(BookingService bookingService, JwtUtils jwtUtils) {
        this.bookingService = bookingService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/create")
    public SingleBooking createBooking(@RequestHeader("Authorization") String token, @RequestParam String restaurantId,
                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                       @RequestParam Integer period, @RequestParam Integer selectedInterval,
                                       @RequestParam Integer numOfSeats, @RequestParam Integer totalSeats, @RequestParam String userPhone) {
        var username = jwtUtils.getUsernameFromToken(token.substring(7));

        return bookingService.createBooking(restaurantId, username, date, period, selectedInterval, numOfSeats, totalSeats, userPhone);
    }
}
