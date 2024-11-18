package org.theatremanagement.show.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.theatremanagement.screen.model.Screen;


import java.time.LocalDateTime;

@Data
@Entity
public class Show {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private LocalDateTime showTime;

    @ManyToOne
    @JoinColumn(name = "screen_id")
    private Screen screen;

    // Getters and Setters
}
