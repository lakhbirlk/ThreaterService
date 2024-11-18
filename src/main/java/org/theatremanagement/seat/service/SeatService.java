package org.theatremanagement.seat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.theatremanagement.seat.model.Seat;
import org.theatremanagement.seat.repository.SeatRepository;
import org.theatremanagement.show.model.Show;


import java.util.List;

// SeatService.java
@Service
public class SeatService {
    @Autowired
    private SeatRepository repository;

    public Seat addSeat(Long showId, Seat seat) {
        Show show = new Show();
        show.setId(showId);
        seat.setShow(show);
        return repository.save(seat);
    }

    public List<Seat> getSeatsByShow(Long showId) {
        return repository.findByShowId(showId);
    }

    public Seat updateSeat(Long seatId, Seat updatedSeat) {
        Seat seat = repository.findById(seatId).orElseThrow(() -> new RuntimeException("controller.org.screenmanagement.seat.Seat not found"));
        seat.setAvailable(updatedSeat.isAvailable());
        return repository.save(seat);
    }

    public void deleteSeat(Long seatId) {
        repository.deleteById(seatId);
    }
}
