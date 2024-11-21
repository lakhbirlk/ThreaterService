package org.theatremanagement.slot.model;

import jakarta.persistence.*;
import lombok.Data;
import org.theatremanagement.seat.model.SeatAvailabilityStatus;
import org.theatremanagement.show.model.Show;

import java.time.LocalDateTime;


// controller.org.screenmanagement.seat.Seat.java
@Data
@Entity
public class Slot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long slotNumber;
    private String seatId;
    @Enumerated(EnumType.STRING)
    private SlotAvailabilityStatus Status;
    private int seatNumber;
    private Long screenID;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int slotLength;
    @ManyToOne
    @JoinColumn(name = "show_id")
    private Show show;

    // Getters and Setters
}
