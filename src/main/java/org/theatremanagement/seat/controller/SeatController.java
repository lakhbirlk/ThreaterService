package org.theatremanagement.seat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.theatremanagement.seat.model.Seat;
import org.theatremanagement.seat.service.SeatService;


import java.util.List;

// SeatController.java
@RestController
@RequestMapping("/api/v1")
public class SeatController {
    @Autowired
    private SeatService service;

    @PostMapping("/shows/{showId}/seats")
    public Seat addSeat(@PathVariable Long showId, @RequestBody Seat seat) {
        return service.addSeat(showId, seat);
    }

    @GetMapping("/shows/{showId}/seats")
    public List<Seat> getSeatsByShow(@PathVariable Long showId) {
        return service.getSeatsByShow(showId);
    }

    @PutMapping("/seats/{seatId}")
    public Seat updateSeat(@PathVariable Long seatId, @RequestBody Seat seat) {
        return service.updateSeat(seatId, seat);
    }

    @DeleteMapping("/seats/{seatId}")
    public void deleteSeat(@PathVariable Long seatId) {
        service.deleteSeat(seatId);
    }
}
