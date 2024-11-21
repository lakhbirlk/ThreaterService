package org.theatremanagement.slot.model;

public enum SlotAvailabilityStatus {
    AVAILABLE,
    RESERVED,
    OCCUPIED,
    OUT_OF_SERVICE, BOOKED;

    @Override
    public String toString() {
        // Capitalize each word for better readability
        return name().charAt(0) + name().substring(1).toLowerCase().replace("_", " ");
    }
}