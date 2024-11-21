package org.theatremanagement.slot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.theatremanagement.kafka.event.ScreenChartCreatedEvent;
import org.theatremanagement.slot.model.Slot;
import org.theatremanagement.slot.model.SlotAvailabilityStatus;
import org.theatremanagement.slot.repository.SlotRepository;
import org.theatremanagement.show.model.Show;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class SlotService {

    private static final Logger logger = LoggerFactory.getLogger(SlotService.class);

    @Autowired
    private SlotRepository repository;

    public Slot addSlot(Long showId, Slot slot) {
        logger.info("Adding slot for show with ID: {}", showId);
        Show show = new Show();
        show.setId(showId);
        slot.setShow(show);
        Slot savedSlot = repository.save(slot);
        logger.info("Slot added with ID: {}", savedSlot.getId());
        return savedSlot;
    }

    @KafkaListener(topics = "screenChart.created", groupId = "theatre-group")
    public void createSlots(ScreenChartCreatedEvent event) {
        logger.info("Processing ScreenChartCreatedEvent for show ID: {}", event.getShowId());
        for (int col = 1; col <= event.getCapacity(); col++) {
            Slot slot = new Slot();
            slot.setSlotNumber(event.getShowSlotNumber());
            slot.setScreenID(event.getScreenId());
            Show show = new Show();
            show.setId(event.getShowId());
            slot.setShow(show);
            slot.setStatus(SlotAvailabilityStatus.AVAILABLE);
            slot.setSeatNumber(col);
            slot.setSlotLength(event.getLength());
            slot.setStartTime(event.getStartTime());
            slot.setEndTime(event.getStartTime().plusMinutes(event.getLength()));

            // Save slot
            Slot savedSlot = repository.save(slot);
            logger.info("Slot created with ID: {}", savedSlot.getId());
        }
    }

    public List<Slot> getSlotsByShow(Long showId) {
        logger.info("Fetching slots for show with ID: {}", showId);
        List<Slot> slots = repository.findByShowId(showId);
        logger.info("Found {} slots for show with ID: {}", slots.size(), showId);
        return slots;
    }

    public Slot updateSlot(Long slotId, Slot updatedSlot) {
        logger.info("Updating slot with ID: {}", slotId);
        Slot slot = repository.findById(slotId)
                .orElseThrow(() -> {
                    logger.error("Slot not found with ID: {}", slotId);
                    return new RuntimeException("Slot not found");
                });
        slot.setStatus(updatedSlot.getStatus());
        Slot updated = repository.save(slot);
        logger.info("Slot with ID: {} updated successfully", updated.getId());
        return updated;
    }

    public void deleteSlot(Long slotId) {
        logger.info("Deleting slot with ID: {}", slotId);
        repository.deleteById(slotId);
        logger.info("Slot with ID: {} deleted successfully", slotId);
    }
}
