package org.theatremanagement.slot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.theatremanagement.slot.model.Slot;
import org.theatremanagement.slot.service.SlotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

// SlotController.java
@RestController
@RequestMapping("/api/v1")
public class SlotController {

    private static final Logger logger = LoggerFactory.getLogger(SlotController.class);

    @Autowired
    private SlotService service;

    @PostMapping("/shows/{showId}/Slots")
    public Slot addSlot(@PathVariable Long showId, @RequestBody Slot slot) {
        logger.info("Request to add slot for show with ID: {}", showId);
        Slot savedSlot = service.addSlot(showId, slot);
        logger.info("Slot added with ID: {}", savedSlot.getId());
        return savedSlot;
    }

    @GetMapping("/shows/{showId}/Slot/{slotId}")
    public Slot getSlotById(@PathVariable Long showId, @PathVariable Long slotId) {
        logger.info("Fetching slot with ID: {} for show with ID: {}", slotId, showId);
        Slot slot = service.getSlotsByShow(showId).stream()
                .filter(s -> s.getId().equals(slotId))
                .findFirst()
                .orElse(null);

        if (slot != null) {
            logger.info("Slot found with ID: {}", slot.getId());
        } else {
            logger.warn("Slot with ID: {} not found for show with ID: {}", slotId, showId);
        }

        return slot;
    }

    @GetMapping("/shows/{showId}/Slots")
    public List<Slot> getSlotsByShow(@PathVariable Long showId) {
        logger.info("Fetching all slots for show with ID: {}", showId);
        List<Slot> slots = service.getSlotsByShow(showId);
        logger.info("Found {} slots for show with ID: {}", slots.size(), showId);
        return slots;
    }

    @PutMapping("/Slots/{slotId}")
    public Slot updateSlot(@PathVariable Long slotId, @RequestBody Slot slot) {
        logger.info("Request to update slot with ID: {}", slotId);
        Slot updatedSlot = service.updateSlot(slotId, slot);
        logger.info("Slot with ID: {} updated successfully", updatedSlot.getId());
        return updatedSlot;
    }

    @DeleteMapping("/Slots/{slotId}")
    public void deleteSlot(@PathVariable Long slotId) {
        logger.info("Request to delete slot with ID: {}", slotId);
        service.deleteSlot(slotId);
        logger.info("Slot with ID: {} deleted successfully", slotId);
    }
}
