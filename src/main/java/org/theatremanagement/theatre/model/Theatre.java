package org.theatremanagement.theatre.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.theatremanagement.screen.model.Screen;

import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@Entity
public class Theatre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    private int numberOfScreen;

    private List<Integer> moviesOnSCreen=new ArrayList<>();

    private List<Integer>  upcomingMovies=new ArrayList<>();

    @OneToMany(mappedBy = "theatre", orphanRemoval = true)
    private List<Screen> screens = new ArrayList<>();

    public Theatre(Long id) {
        this.id = id;
    }

    public Theatre() {
    }


    // Getters and Setters
}
