package org.theatremanagement.seat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.theatremanagement.seat.model.Seat;


import java.util.List;

// SeatRepository.java
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByShowId(Long showId);
}
