package org.theatremanagement.show.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.theatremanagement.screen.model.Screen;


import java.time.LocalDateTime;

@Data
@Document(indexName = "show")
public class ShowDoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private LocalDateTime showTime;

    private Long screenId;

    // Getters and Setters
}
