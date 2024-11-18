package org.theatremanagement.booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.theatremanagement.booking.model.Booking;
import org.theatremanagement.exception.BookingException;
import org.theatremanagement.booking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // Endpoint to create a booking
    @PostMapping("/create")
    public ResponseEntity<?> createBooking(@RequestParam Long showId,
                                           @RequestParam List<Long> seatIds,
                                           @RequestParam Long userId) {
        try {
            Booking booking = bookingService.createBooking(showId, seatIds, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(booking);
        } catch (BookingException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    // Endpoint to confirm a booking
    @PostMapping("/confirm/{bookingId}")
    public ResponseEntity<?> confirmBooking(@PathVariable Long bookingId) {
        try {
            // Add logic to confirm the booking
            return ResponseEntity.status(HttpStatus.OK).body("Booking confirmed!");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error confirming the booking.");
        }
    }
}
