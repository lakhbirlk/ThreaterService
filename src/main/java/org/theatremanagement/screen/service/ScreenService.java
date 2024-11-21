package org.theatremanagement.screen.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.theatremanagement.kafka.KafkaProducerService;
import org.theatremanagement.kafka.event.ScreenCreatedEvent;
import org.theatremanagement.screen.mapper.ScreenToScreenDocMapper;
import org.theatremanagement.screen.model.Screen;
import org.theatremanagement.screen.repository.ScreenElasticsearchRepository;
import org.theatremanagement.screen.repository.ScreenRepository;
import org.theatremanagement.theatre.model.Theatre;
import org.theatremanagement.kafka.event.TheatreCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class ScreenService {

    private static final Logger logger = LoggerFactory.getLogger(ScreenService.class);

    @Autowired
    private ScreenRepository repository;

    @Autowired
    private ScreenElasticsearchRepository screenElasticsearchRepository; // Elasticsearch Repository

    @Autowired
    private KafkaProducerService kafkaProducerService;

    public Screen addscreen(Screen screen) {
        logger.info("Adding screen: {}", screen); // Log when adding a screen
        Screen savedScreen = repository.save(screen);
        logger.info("Screen added successfully: {}", savedScreen); // Log after screen is added
        return savedScreen;
    }

    @KafkaListener(topics = "theatre.created", groupId = "theatre-group")
    public void createScreens(TheatreCreatedEvent event) {
        logger.info("Received TheatreCreatedEvent for theatreId: {} with {} screens", event.getTheatreId(), event.getNumberOfScreen());
        // Create a predefined set of screens for the theatre
        for (int i = 1; i <= event.getNumberOfScreen(); i++) {
            Screen screen = new Screen();
            screen.setName("Screen " + i);
            screen.setSeatingCapacity(10);
            screen.setTheatre(new Theatre(event.getTheatreId()));

            Screen savedScreen = repository.save(screen);
            logger.info("Screen created: {}", savedScreen);

            // Publish an event for each screen
            ScreenCreatedEvent screenEvent = new ScreenCreatedEvent();
            screenEvent.setScreenId(savedScreen.getId());
            screenEvent.setTheatreId(event.getTheatreId());
            screenEvent.setScreenName(savedScreen.getName());
            screenEvent.setCapacity(savedScreen.getSeatingCapacity());

            kafkaProducerService.publishEvent("screen.created", screenEvent);
            logger.info("ScreenCreatedEvent published for screenId: {}", savedScreen.getId());
        }
    }

    public Screen getscreen(Long id) {
        logger.info("Fetching screen with ID: {}", id);
        Screen screen = repository.findById(id).orElseThrow(() -> {
            logger.error("Screen with ID: {} not found", id);
            return new RuntimeException("screen not found");
        });
        logger.info("Screen found: {}", screen);
        return screen;
    }

    public Screen updatescreen(Long id, Screen updatedscreen) {
        logger.info("Updating screen with ID: {}", id);
        Screen screen = getscreen(id);
        screen.setName(updatedscreen.getName());
        screen.setSeatingCapacity(updatedscreen.getSeatingCapacity());
        ScreenToScreenDocMapper.toScreenDoc(screen);
        Screen updatedScreen = repository.save(screen);
        logger.info("Screen updated successfully: {}", updatedScreen);
        return updatedScreen;
    }

    public void deleteScreen(Long id) {
        logger.info("Deleting screen with ID: {}", id);
        repository.deleteById(id);
        screenElasticsearchRepository.deleteById(id);
        logger.info("Screen with ID: {} deleted from both repository and Elasticsearch", id);
    }

    public Screen saveScreen(Screen screen) {
        logger.info("Saving screen: {}", screen);
        repository.save(screen);
        screenElasticsearchRepository.save(ScreenToScreenDocMapper.toScreenDoc(screen)); // Save to Elasticsearch
        logger.info("Screen saved to repository and Elasticsearch: {}", screen);
        return screen;
    }

    public List<Screen> searchByName(String name) {
        logger.info("Searching for screens with name containing: {}", name);
        List<Screen> screens = screenElasticsearchRepository.findByNameContaining(name)
                .stream()
                .map(ScreenToScreenDocMapper::toScreen)
                .toList();
        logger.info("Found {} screens with name containing '{}'", screens.size(), name);
        return screens;
    }
}
