package org.theatremanagement.seat.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.theatremanagement.show.model.Show;


// controller.org.screenmanagement.seat.Seat.java
@Data
@Entity
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String seatNumber;
    @Enumerated(EnumType.STRING)
    private SeatAvailabilityStatus Status;

    private Long screenID;
    @ManyToOne
    @JoinColumn(name = "show_id")
    private Show show;

    // Getters and Setters
}
