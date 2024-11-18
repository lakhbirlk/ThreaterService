package org.theatremanagement.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.theatremanagement.exception.BookingException;
import org.theatremanagement.booking.Respository.BookingRepository;
import org.theatremanagement.User.User;
import org.theatremanagement.booking.model.Booking;
import org.theatremanagement.seat.model.Seat;
import org.theatremanagement.seat.repository.SeatRepository;
import org.theatremanagement.show.model.Show;
import org.theatremanagement.show.repository.ShowRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private ShowRepository showRepository;

    public Booking createBooking(Long showId, List<Long> seatIds, Long userId) throws BookingException {
        // Check if the show exists
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new BookingException("Show not found"));

        // Check if all the selected seats are available
        List<Seat> seats = seatRepository.findAllById(seatIds);
        for (Seat seat : seats) {
            if (!seat.isAvailable()) {
                throw new BookingException("Seat " + seat.getSeatNumber() + " is not available.");
            }
        }

        // Create the booking
        LocalDateTime bookingTime = LocalDateTime.now();
        List<Booking> bookings = new ArrayList<>();
        for (Seat seat : seats) {
            Booking booking = new Booking();
            booking.setShow(show);
            booking.setSeat(seat);
            booking.setUser(new User());  // Assuming you have a User constructor
            booking.setBookingTime(bookingTime);
            booking.setConfirmed(false);  // Initially, the booking is not confirmed
            bookings.add(booking);

            // Mark the seat as unavailable
            seat.setAvailable(false);
            seatRepository.save(seat);
        }
        // Save bookings
        return bookingRepository.saveAll(bookings).get(0);  // Return the first booking
    }
}
