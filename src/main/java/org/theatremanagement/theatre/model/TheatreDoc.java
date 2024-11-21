package org.theatremanagement.theatre.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.theatremanagement.screen.model.Screen;

import java.util.ArrayList;
import java.util.List;
@Data
@Document(indexName = "theatre")
public class TheatreDoc {
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @OneToMany(mappedBy = "theatre", orphanRemoval = true)
    private List<Screen> screens = new ArrayList<>();

    // Getters and Setters
}
