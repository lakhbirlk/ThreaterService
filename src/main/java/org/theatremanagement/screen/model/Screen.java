package org.theatremanagement.screen.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.theatremanagement.theatre.model.Theatre;

import java.util.ArrayList;
import java.util.List;

// screen.java

@Data
@Entity
public class Screen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int seatingCapacity;
    @ManyToOne
    @JoinColumn(name = "theatre_id")
    @JsonBackReference // to avoid infinite recursion
    private Theatre theatre;

    @OneToMany(mappedBy = "screen", orphanRemoval = true)
    private List<ScreenChart> screenCharts = new ArrayList<>();
    // Getters and Setters
}
