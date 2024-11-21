package org.theatremanagement.kafka.event;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShowCreatedEvent {
    Long screenId;
    int capacity;
    Long showSlotNumber;
    Long showId;
    LocalDateTime startTime;
    int length;
}
