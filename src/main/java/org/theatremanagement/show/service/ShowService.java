package org.theatremanagement.show.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.theatremanagement.kafka.KafkaProducerService;
import org.theatremanagement.kafka.event.ShowCreatedEvent;
import org.theatremanagement.screen.model.Screen;
import org.theatremanagement.screen.repository.ScreenRepository;
import org.theatremanagement.show.mapper.ShowToShowDocMapper;
import org.theatremanagement.show.model.Show;
import org.theatremanagement.show.repository.ShowElasticsearchRepository;
import org.theatremanagement.show.repository.ShowRepository;

import java.util.List;

@Service
public class ShowService {

    private static final Logger logger = LoggerFactory.getLogger(ShowService.class);

    @Autowired
    public ShowRepository repository;

    @Autowired
    public ScreenRepository screenRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private ShowElasticsearchRepository showElasticsearchRepository;

    public Show addShow(Long screenId, Show show1) {
        logger.info("Adding a new show for screen with ID: {}", screenId);
        Show show = new Show();
        show.setShowLength(show1.getShowLength());
        show.setMovieID(show1.getMovieID());
        show.setShowTime(show1.getShowTime());
        show.setDurationDays(show1.getDurationDays());
        show.setTitle(show.getTitle());

        Screen screen = screenRepository.findById(screenId).orElseThrow(() -> new RuntimeException("Screen not found"));
        show.setScreenId(screenId);

        Show savedShow = repository.save(show);
        logger.info("Show added successfully with ID: {}", savedShow.getId());

        // Create event and send via Kafka for each day of the show
        for (int i = 0; i < show.getDurationDays(); i++) {
            ShowCreatedEvent event = new ShowCreatedEvent();
            event.setScreenId(screenId);
            event.setShowId(savedShow.getId());
            event.setCapacity(screen.getSeatingCapacity());
            event.setStartTime(show.getShowTime().plusDays(i));
            event.setLength(show.getShowLength());
            kafkaProducerService.publishEvent("show.created", event);
            logger.info("Published show created event for show with ID: {} on day: {}", savedShow.getId(), i+1);
        }
        return savedShow;
    }

    public Show saveShow(Show show) {
        logger.info("Saving show with ID: {}", show.getId());
        repository.save(show);
        showElasticsearchRepository.save(ShowToShowDocMapper.toShowDoc(show));
        logger.info("Show saved to both DB and Elasticsearch: {}", show.getId());
        return show;
    }

    public List<Show> searchByName(String title) {
        logger.info("Searching shows by title: {}", title);
        List<Show> shows = showElasticsearchRepository.findByTitleContaining(title).stream()
                .map(ShowToShowDocMapper::toShow)
                .toList();
        logger.info("Found {} shows with title: {}", shows.size(), title);
        return shows;
    }

    public List<Show> searchByShowTime(String showTime) {
        logger.info("Searching shows by show time: {}", showTime);
        List<Show> shows = showElasticsearchRepository.findByShowTime(showTime).stream()
                .map(ShowToShowDocMapper::toShow)
                .toList();
        logger.info("Found {} shows with show time: {}", shows.size(), showTime);
        return shows;
    }

    public List<Show> getShowsByScreen(Long screenId) {
        logger.info("Fetching shows for screen with ID: {}", screenId);
        List<Show> shows = repository.findByscreenId(screenId);
        logger.info("Found {} shows for screen with ID: {}", shows.size(), screenId);
        return shows;
    }

    public Show getShow(Long id) {
        logger.info("Fetching show with ID: {}", id);
        Show show = repository.findById(id).orElseThrow(() -> new RuntimeException("Show not found"));
        logger.info("Found show with ID: {}", id);
        return show;
    }

    public Show updateShow(Long id, Show updatedShow) {
        logger.info("Updating show with ID: {}", id);
        Show show = getShow(id);
        show.setTitle(updatedShow.getTitle());
        show.setShowTime(updatedShow.getShowTime());
        showElasticsearchRepository.save(ShowToShowDocMapper.toShowDoc(show));
        Show savedShow = repository.save(show);
        logger.info("Updated show with ID: {}", savedShow.getId());
        return savedShow;
    }

    public void deleteShow(Long id) {
        logger.info("Deleting show with ID: {}", id);
        showElasticsearchRepository.deleteById(id);
        repository.deleteById(id);
        logger.info("Show with ID: {} deleted successfully", id);
    }
}
