package org.theatremanagement.kafka.event;

import lombok.Data;

@Data
public class ScreenCreatedEvent {
    private Long screenId;
    private Long theatreId;
    private String screenName;
    private int capacity;

    // Getters and Setters
}