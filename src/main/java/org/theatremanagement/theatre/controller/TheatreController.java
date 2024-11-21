package org.theatremanagement.theatre.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.theatremanagement.theatre.service.TheatreService;
import org.theatremanagement.theatre.model.Theatre;

import java.util.List;

@RestController
@RequestMapping("/api/v1/theatres")
public class TheatreController {

    private static final Logger logger = LoggerFactory.getLogger(TheatreController.class);

    private final TheatreService theatreService;

    public TheatreController(TheatreService theatreService) {
        this.theatreService = theatreService;
    }

    @PostMapping
    public ResponseEntity<Theatre> createTheatre(@RequestBody Theatre theatre) {
        logger.info("Creating new theatre: {}", theatre.getName());
        Theatre createdTheatre = theatreService.addTheatre(theatre);
        logger.info("Theatre created successfully with ID: {}", createdTheatre.getId());
        return new ResponseEntity<>(createdTheatre, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Theatre>> getAllTheatres() {
        logger.info("Fetching all theatres");
        List<Theatre> theatres = theatreService.getAllTheatres();
        logger.info("Found {} theatres", theatres.size());
        return new ResponseEntity<>(theatres, HttpStatus.OK);
    }

    @GetMapping("/{theatreId}")
    public ResponseEntity<Theatre> getTheatre(@PathVariable Long theatreId) {
        logger.info("Fetching theatre with ID: {}", theatreId);
        Theatre theatre = theatreService.getTheatre(theatreId);
        logger.info("Found theatre with ID: {}", theatreId);
        return new ResponseEntity<>(theatre, HttpStatus.OK);
    }

    @PutMapping("/{theatreId}")
    public ResponseEntity<Theatre> updateTheatre(@PathVariable Long theatreId, @RequestBody Theatre theatre) {
        logger.info("Updating theatre with ID: {}", theatreId);
        Theatre updatedTheatre = theatreService.updateTheatre(theatreId, theatre);
        logger.info("Theatre with ID: {} updated successfully", theatreId);
        return new ResponseEntity<>(updatedTheatre, HttpStatus.OK);
    }

    @DeleteMapping("/{theatreId}")
    public ResponseEntity<Void> deleteTheatre(@PathVariable Long theatreId) {
        logger.info("Deleting theatre with ID: {}", theatreId);
        theatreService.deleteTheatre(theatreId);
        logger.info("Theatre with ID: {} deleted successfully", theatreId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/search")
    public List<Theatre> searchTheatres(@RequestParam String name) {
        logger.info("Searching theatres by name: {}", name);
        List<Theatre> theatres = theatreService.searchByName(name);
        logger.info("Found {} theatres with name: {}", theatres.size(), name);
        return theatres;
    }

    @GetMapping("/search/location")
    public List<Theatre> searchByLocation(@RequestParam String location) {
        logger.info("Searching theatres by location: {}", location);
        List<Theatre> theatres = theatreService.searchByLocation(location);
        logger.info("Found {} theatres in location: {}", theatres.size(), location);
        return theatres;
    }
}
