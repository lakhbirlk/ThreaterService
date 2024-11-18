package org.theatremanagement.screen.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.theatremanagement.theatre.Theatre;

    // screen.java
    @Document(indexName = "screen")
    @Data
    public class ScreenDoc {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;
        private int seatingCapacity;
        @ManyToOne
        @JoinColumn(name = "theatre_id")
        @JsonBackReference // to avoid infinite recursion
        private Theatre theatre;

        // Getters and Setters
    }


