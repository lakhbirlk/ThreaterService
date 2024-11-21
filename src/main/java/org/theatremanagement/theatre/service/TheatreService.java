package org.theatremanagement.theatre.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.theatremanagement.kafka.KafkaProducerService;
import org.theatremanagement.kafka.event.TheatreCreatedEvent;
import org.theatremanagement.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.theatremanagement.theatre.mapper.TheatreToTheatreDocMapper;
import org.theatremanagement.theatre.model.Theatre;
import org.theatremanagement.theatre.repository.TheatreElasticsearchRepository;
import org.theatremanagement.theatre.repository.TheatreRepository;

import java.util.List;

@Service
public class TheatreService {

    private static final Logger logger = LoggerFactory.getLogger(TheatreService.class);

    @Autowired
    private TheatreRepository theatreRepository;

    @Autowired
    private TheatreElasticsearchRepository theatreElasticsearchRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    /**
     * Add a new theatre
     *
     * @param theatre Theatre entity
     * @return The saved Theatre entity
     */
    public Theatre addTheatre(Theatre theatre) {
        logger.info("Adding a new theatre with name: {}", theatre.getName());
        Theatre savedTheatre = theatreRepository.save(theatre);
        theatreElasticsearchRepository.save(TheatreToTheatreDocMapper.toTheatreDoc(theatre)); // Save to Elasticsearch

        // Publish event
        TheatreCreatedEvent event = new TheatreCreatedEvent();
        event.setTheatreId(savedTheatre.getId());
        event.setTheatreName(savedTheatre.getName());
        event.setLocation(savedTheatre.getLocation());
        event.setNumberOfScreen(savedTheatre.getNumberOfScreen());

        kafkaProducerService.publishEvent("theatre.created", event);

        logger.info("Theatre created with ID: {}", savedTheatre.getId());
        return savedTheatre;
    }

    /**
     * Retrieve all theatres
     *
     * @return List of theatres
     */
    public List<Theatre> getAllTheatres() {
        logger.info("Fetching all theatres from database");
        List<Theatre> theatres = theatreRepository.findAll();
        logger.info("Found {} theatres", theatres.size());
        return theatres;
    }

    /**
     * Retrieve a specific theatre by ID
     *
     * @param theatreId Theatre ID
     * @return The Theatre entity
     * @throws ResourceNotFoundException if the theatre does not exist
     */
    public Theatre getTheatre(Long theatreId) {
        logger.info("Fetching theatre with ID: {}", theatreId);
        return theatreRepository.findById(theatreId)
                .orElseThrow(() -> {
                    logger.error("Theatre not found with id: {}", theatreId);
                    return new ResourceNotFoundException("Theatre not found with id: " + theatreId);
                });
    }

    /**
     * Update theatre details
     *
     * @param theatreId Theatre ID
     * @param updatedTheatre Updated Theatre entity
     * @return The updated Theatre entity
     * @throws ResourceNotFoundException if the theatre does not exist
     */
    public Theatre updateTheatre(Long theatreId, Theatre updatedTheatre) {
        logger.info("Updating theatre with ID: {}", theatreId);
        Theatre existingTheatre = theatreRepository.findById(theatreId)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found with id: " + theatreId));

        existingTheatre.setName(updatedTheatre.getName());
        existingTheatre.setLocation(updatedTheatre.getLocation());

        // Save updated theatre
        Theatre savedTheatre = theatreRepository.save(existingTheatre);
        logger.info("Theatre with ID: {} updated successfully", theatreId);
        return savedTheatre;
    }

    /**
     * Delete a theatre by ID
     *
     * @param theatreId Theatre ID
     * @throws ResourceNotFoundException if the theatre does not exist
     */
    public void deleteTheatre(Long theatreId) {
        logger.info("Deleting theatre with ID: {}", theatreId);
        if (!theatreRepository.existsById(theatreId)) {
            logger.error("Theatre not found with id: {}", theatreId);
            throw new ResourceNotFoundException("Theatre not found with id: " + theatreId);
        }
        theatreRepository.deleteById(theatreId);
        logger.info("Theatre with ID: {} deleted successfully", theatreId);
    }

    public List<Theatre> searchByName(String name) {
        logger.info("Searching theatres by name: {}", name);
        List<Theatre> theatres = theatreElasticsearchRepository.findByNameContaining(name);
        logger.info("Found {} theatres matching the name: {}", theatres.size(), name);
        return theatres;
    }

    public List<Theatre> searchByLocation(String location) {
        logger.info("Searching theatres by location: {}", location);
        List<Theatre> theatres = theatreElasticsearchRepository.findByLocationContaining(location);
        logger.info("Found {} theatres in location: {}", theatres.size(), location);
        return theatres;
    }
}
