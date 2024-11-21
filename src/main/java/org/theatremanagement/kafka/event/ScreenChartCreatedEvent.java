package org.theatremanagement.kafka.event;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScreenChartCreatedEvent {
    Long screenId;
    int capacity;
    Long showSlotNumber;
    Long showId;
    LocalDateTime startTime;
    int length;
}
