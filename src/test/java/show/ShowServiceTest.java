package show;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.theatremanagement.kafka.KafkaProducerService;
import org.theatremanagement.kafka.event.ShowCreatedEvent;
import org.theatremanagement.screen.model.Screen;
import org.theatremanagement.show.model.Show;
import org.theatremanagement.show.repository.ShowElasticsearchRepository;
import org.theatremanagement.show.repository.ShowRepository;
import org.theatremanagement.screen.repository.ScreenRepository;
import org.theatremanagement.show.service.ShowService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShowServiceTest {

    @Mock
    private ShowRepository showRepository;

    @Mock
    private ScreenRepository screenRepository;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @Mock
    private ShowElasticsearchRepository showElasticsearchRepository;

    @InjectMocks
    private ShowService showService;

    private Show show;
    private Screen screen;

    @BeforeEach
    public void setUp() {
        screen = new Screen();
        screen.setId(1L);
        screen.setSeatingCapacity(100);

        show = new Show();
        show.setId(1L);
        show.setTitle("Movie Show");
        show.setShowLength(120);
        show.setMovieID(1L);
        show.setShowTime(LocalDateTime.now());
        show.setDurationDays(1);
    }

    @Test
    public void testAddShow() {
        when(screenRepository.findById(1L)).thenReturn(Optional.of(screen));
        when(showRepository.save(any(Show.class))).thenReturn(show);

        Show result = showService.addShow(1L, show);

        verify(showRepository, times(1)).save(any(Show.class));
        verify(kafkaProducerService, times(1)).publishEvent(anyString(), any(ShowCreatedEvent.class));
        assert(result.getId() != null);
    }

    @Test
    public void testSaveShow() {
        when(showRepository.save(any(Show.class))).thenReturn(show);
        when(showElasticsearchRepository.save(any())).thenReturn(null);

        Show result = showService.saveShow(show);

        verify(showRepository, times(1)).save(any(Show.class));
        verify(showElasticsearchRepository, times(1)).save(any());
        assert(result.getId() == 1L);
    }



    @Test
    public void testUpdateShow() {
        when(showRepository.findById(1L)).thenReturn(Optional.of(show));
        when(showRepository.save(any(Show.class))).thenReturn(show);

        show.setTitle("Updated Movie Show");
        Show result = showService.updateShow(1L, show);

        verify(showRepository, times(1)).save(any(Show.class));
        assert(result.getTitle().equals("Updated Movie Show"));
    }

    @Test
    public void testDeleteShow() {
        when(showRepository.findById(1L)).thenReturn(Optional.of(show));

        showService.deleteShow(1L);

        verify(showRepository, times(1)).deleteById(1L);
        verify(showElasticsearchRepository, times(1)).deleteById(1L);
    }
}
