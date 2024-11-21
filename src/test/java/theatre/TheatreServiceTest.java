package theatre;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.theatremanagement.kafka.KafkaProducerService;
import org.theatremanagement.kafka.event.TheatreCreatedEvent;
import org.theatremanagement.exception.ResourceNotFoundException;

import org.theatremanagement.theatre.model.Theatre;
import org.theatremanagement.theatre.repository.TheatreElasticsearchRepository;
import org.theatremanagement.theatre.repository.TheatreRepository;
import org.theatremanagement.theatre.service.TheatreService;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TheatreServiceTest {

    @Mock
    private TheatreRepository theatreRepository;

    @Mock
    private TheatreElasticsearchRepository theatreElasticsearchRepository;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @InjectMocks
    private TheatreService theatreService;

    private Theatre theatre;

    @BeforeEach
    public void setUp() {
        theatre = new Theatre();
        theatre.setId(1L);
        theatre.setName("Royal Theatre");
        theatre.setLocation("New York");
        theatre.setNumberOfScreen(5);
    }

    @Test
    public void testAddTheatre() {
        when(theatreRepository.save(any(Theatre.class))).thenReturn(theatre);

        Theatre savedTheatre = theatreService.addTheatre(theatre);

        assertNotNull(savedTheatre);
        assertEquals("Royal Theatre", savedTheatre.getName());
        assertEquals("New York", savedTheatre.getLocation());

        verify(theatreRepository, times(1)).save(any(Theatre.class));
        verify(kafkaProducerService, times(1)).publishEvent(anyString(), any(TheatreCreatedEvent.class));
    }

    @Test
    public void testGetAllTheatres() {
        when(theatreRepository.findAll()).thenReturn(Arrays.asList(theatre));

        var theatres = theatreService.getAllTheatres();

        assertFalse(theatres.isEmpty());
        assertEquals(1, theatres.size());

        verify(theatreRepository, times(1)).findAll();
    }

    @Test
    public void testGetTheatre() {
        when(theatreRepository.findById(1L)).thenReturn(Optional.of(theatre));

        Theatre foundTheatre = theatreService.getTheatre(1L);

        assertNotNull(foundTheatre);
        assertEquals("Royal Theatre", foundTheatre.getName());

        verify(theatreRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetTheatre_NotFound() {
        when(theatreRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            theatreService.getTheatre(1L);
        });

        assertEquals("Theatre not found with id: 1", exception.getMessage());
    }

    @Test
    public void testUpdateTheatre() {
        Theatre updatedTheatre = new Theatre();
        updatedTheatre.setName("Updated Royal Theatre");
        updatedTheatre.setLocation("Los Angeles");

        when(theatreRepository.findById(1L)).thenReturn(Optional.of(theatre));
        when(theatreRepository.save(any(Theatre.class))).thenReturn(updatedTheatre);

        Theatre result = theatreService.updateTheatre(1L, updatedTheatre);

        assertNotNull(result);
        assertEquals("Updated Royal Theatre", result.getName());
        assertEquals("Los Angeles", result.getLocation());

        verify(theatreRepository, times(1)).save(any(Theatre.class));
    }

    @Test
    public void testDeleteTheatre() {
        when(theatreRepository.existsById(1L)).thenReturn(true);

        theatreService.deleteTheatre(1L);

        verify(theatreRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteTheatre_NotFound() {
        when(theatreRepository.existsById(1L)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            theatreService.deleteTheatre(1L);
        });

        assertEquals("Theatre not found with id: 1", exception.getMessage());
    }


}
