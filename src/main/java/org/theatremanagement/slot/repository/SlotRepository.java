package org.theatremanagement.slot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.theatremanagement.seat.model.Seat;
import org.theatremanagement.slot.model.Slot;


import java.util.List;

// SeatRepository.java
public interface SlotRepository extends JpaRepository<Slot, Long> {
    List<Slot> findByShowId(Long showId);
}
