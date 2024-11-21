package screen;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.theatremanagement.kafka.KafkaProducerService;
import org.theatremanagement.kafka.event.ScreenCreatedEvent;
import org.theatremanagement.screen.model.Screen;
import org.theatremanagement.screen.repository.ScreenElasticsearchRepository;
import org.theatremanagement.screen.repository.ScreenRepository;
import org.theatremanagement.screen.service.ScreenService;
import org.theatremanagement.theatre.model.Theatre;
import org.theatremanagement.kafka.event.TheatreCreatedEvent;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ScreenServiceTest {

    @Mock
    private ScreenRepository screenRepository;

    @Mock
    private ScreenElasticsearchRepository screenElasticsearchRepository;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @InjectMocks
    private ScreenService screenService;

    private Screen screen;
    private TheatreCreatedEvent theatreCreatedEvent;

    @BeforeEach
    public void setUp() {
        screen = new Screen();
        screen.setId(1L);
        screen.setName("Screen 1");
        screen.setSeatingCapacity(100);
        screen.setTheatre(new Theatre(101L));

        theatreCreatedEvent = new TheatreCreatedEvent();
        theatreCreatedEvent.setTheatreId(101L);
        theatreCreatedEvent.setNumberOfScreen(3);
    }

    @Test
    public void testAddScreen() {
        when(screenRepository.save(any(Screen.class))).thenReturn(screen);

        Screen addedScreen = screenService.addscreen(screen);

        assertNotNull(addedScreen);
        assertEquals("Screen 1", addedScreen.getName());
        verify(screenRepository, times(1)).save(screen);
    }

    @Test
    public void testGetScreen() {
        when(screenRepository.findById(1L)).thenReturn(Optional.of(screen));

        Screen fetchedScreen = screenService.getscreen(1L);

        assertNotNull(fetchedScreen);
        assertEquals(1L, fetchedScreen.getId());
        verify(screenRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetScreenNotFound() {
        when(screenRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            screenService.getscreen(1L);
        });

        assertEquals("screen not found", exception.getMessage());
    }

    @Test
    public void testUpdateScreen() {
        when(screenRepository.findById(1L)).thenReturn(Optional.of(screen));
        when(screenRepository.save(any(Screen.class))).thenReturn(screen);

        screen.setName("Updated Screen");
        screen.setSeatingCapacity(120);

        Screen updatedScreen = screenService.updatescreen(1L, screen);

        assertNotNull(updatedScreen);
        assertEquals("Updated Screen", updatedScreen.getName());
        verify(screenRepository, times(1)).save(screen);
    }

    @Test
    public void testDeleteScreen() {
        doNothing().when(screenRepository).deleteById(1L);
        doNothing().when(screenElasticsearchRepository).deleteById(1L);

        screenService.deleteScreen(1L);

        verify(screenRepository, times(1)).deleteById(1L);
        verify(screenElasticsearchRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testSaveScreen() {
        when(screenRepository.save(any(Screen.class))).thenReturn(screen);
        when(screenElasticsearchRepository.save(any())).thenReturn(null); // Mock Elasticsearch save

        Screen savedScreen = screenService.saveScreen(screen);

        assertNotNull(savedScreen);
        verify(screenRepository, times(1)).save(screen);
        verify(screenElasticsearchRepository, times(1)).save(any());
    }


    @Test
    public void testCreateScreensKafkaListener() {
        // Mock the KafkaListener behavior
        when(screenRepository.save(any(Screen.class))).thenReturn(screen);
        doNothing().when(kafkaProducerService).publishEvent(eq("screen.created"), any(ScreenCreatedEvent.class));

        // Call the method directly (simulate the listener)
        screenService.createScreens(theatreCreatedEvent);

        verify(screenRepository, times(3)).save(any(Screen.class)); // Three screens should be created
        verify(kafkaProducerService, times(3)).publishEvent(eq("screen.created"), any(ScreenCreatedEvent.class)); // Event should be published
    }
}
