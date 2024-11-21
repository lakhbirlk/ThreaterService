package org.theatremanagement.screen.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

// screen.java

@Data
@Entity
public class ScreenChart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDateTime date;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long showId;
    @ManyToOne
    @JoinColumn(name = "screen_id")
    @JsonBackReference // to avoid infinite recursion
    private Screen screen;
}
