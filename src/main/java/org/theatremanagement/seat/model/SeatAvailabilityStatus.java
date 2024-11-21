package org.theatremanagement.seat.model;

public enum SeatAvailabilityStatus {
    AVAILABLE,
    RESERVED,
    OCCUPIED,
    OUT_OF_SERVICE;

    @Override
    public String toString() {
        // Capitalize each word for better readability
        return name().charAt(0) + name().substring(1).toLowerCase().replace("_", " ");
    }
}