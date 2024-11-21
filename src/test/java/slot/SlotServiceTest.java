package slot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.theatremanagement.kafka.event.ScreenChartCreatedEvent;
import org.theatremanagement.slot.model.Slot;
import org.theatremanagement.slot.model.SlotAvailabilityStatus;
import org.theatremanagement.slot.repository.SlotRepository;
import org.theatremanagement.show.model.Show;
import org.theatremanagement.slot.service.SlotService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SlotServiceTest {

    @Mock
    private SlotRepository slotRepository;

    @InjectMocks
    private SlotService slotService;

    private Slot slot;
    private ScreenChartCreatedEvent screenChartCreatedEvent;

    @BeforeEach
    public void setUp() {
        slot = new Slot();
        slot.setId(1L);
        slot.setSlotNumber(1L);
        slot.setSeatNumber(1);
        slot.setStatus(SlotAvailabilityStatus.AVAILABLE);
        slot.setSlotLength(120);
        slot.setStartTime(LocalDateTime.now());
        slot.setEndTime(LocalDateTime.now().plusMinutes(120));

        Show show = new Show();
        show.setId(1L);
        slot.setShow(show);

        screenChartCreatedEvent = new ScreenChartCreatedEvent();
        screenChartCreatedEvent.setShowId(1L);
        screenChartCreatedEvent.setScreenId(1L);
        screenChartCreatedEvent.setShowSlotNumber(1L);
        screenChartCreatedEvent.setCapacity(5);
        screenChartCreatedEvent.setLength(120);
        screenChartCreatedEvent.setStartTime(LocalDateTime.now());
    }

    @Test
    public void testAddSlot() {
        when(slotRepository.save(any(Slot.class))).thenReturn(slot);

        Slot savedSlot = slotService.addSlot(1L, slot);

        assertNotNull(savedSlot);
        assertEquals(1L, savedSlot.getId());
        verify(slotRepository, times(1)).save(any(Slot.class));
    }

    @Test
    public void testCreateSlots_KafkaListener() {
        when(slotRepository.save(any(Slot.class))).thenReturn(slot);

        slotService.createSlots(screenChartCreatedEvent);

        verify(slotRepository, times(5)).save(any(Slot.class)); // 5 slots based on capacity
    }

    @Test
    public void testGetSlotsByShow() {
        when(slotRepository.findByShowId(1L)).thenReturn(List.of(slot));

        List<Slot> slots = slotService.getSlotsByShow(1L);

        assertNotNull(slots);
        assertEquals(1, slots.size());
        verify(slotRepository, times(1)).findByShowId(1L);
    }

    @Test
    public void testUpdateSlot() {
        Slot updatedSlot = new Slot();
        updatedSlot.setStatus(SlotAvailabilityStatus.BOOKED);

        when(slotRepository.findById(1L)).thenReturn(Optional.of(slot));
        when(slotRepository.save(any(Slot.class))).thenReturn(updatedSlot);

        Slot updated = slotService.updateSlot(1L, updatedSlot);

        assertNotNull(updated);
        assertEquals(SlotAvailabilityStatus.BOOKED, updated.getStatus());
        verify(slotRepository, times(1)).save(any(Slot.class));
    }

    @Test
    public void testUpdateSlot_NotFound() {
        Slot updatedSlot = new Slot();
        updatedSlot.setStatus(SlotAvailabilityStatus.BOOKED);

        when(slotRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            slotService.updateSlot(1L, updatedSlot);
        });

        assertEquals("Slot not found", exception.getMessage());
    }

    @Test
    public void testDeleteSlot() {
        doNothing().when(slotRepository).deleteById(1L);

        slotService.deleteSlot(1L);

        verify(slotRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteSlot_NotFound() {
        doThrow(new RuntimeException("Slot not found")).when(slotRepository).deleteById(1L);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            slotService.deleteSlot(1L);
        });

        assertEquals("Slot not found", exception.getMessage());
    }
}
