package org.theatremanagement.kafka.event;

import lombok.Data;

@Data
public class TheatreCreatedEvent {

    private Long TheatreId;


    private String TheatreName;


    private String location;

    private int numberOfScreen;

    // Getters and Setters
}
