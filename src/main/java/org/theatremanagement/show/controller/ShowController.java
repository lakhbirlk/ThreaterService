package org.theatremanagement.show.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.theatremanagement.show.model.Show;
import org.theatremanagement.show.service.ShowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ShowController {

    private static final Logger logger = LoggerFactory.getLogger(ShowController.class);

    @Autowired
    private ShowService service;

    // Add a new show for a given screen
    @PostMapping("/screens/{screenId}/shows")
    public Show addShow(@PathVariable Long screenId, @RequestBody Show show) {
        logger.info("Adding new show to screen with ID: {}", screenId);
        Show addedShow = service.addShow(screenId, show);
        logger.info("Show added successfully: {}", addedShow);
        return addedShow;
    }

    // Get shows for a specific screen
    @GetMapping("/screens/{screenId}/shows")
    public List<Show> getShowsByScreen(@PathVariable Long screenId) {
        logger.info("Fetching shows for screen with ID: {}", screenId);
        List<Show> shows = service.getShowsByScreen(screenId);
        if (shows.isEmpty()) {
            logger.warn("No shows found for screen with ID: {}", screenId);
        } else {
            logger.info("Found {} shows for screen with ID: {}", shows.size(), screenId);
        }
        return shows;
    }

    // Get a specific show by its ID
    @GetMapping("/shows/{showId}")
    public Show getShow(@PathVariable Long showId) {
        logger.info("Fetching show with ID: {}", showId);
        Show show = service.getShow(showId);
        if (show != null) {
            logger.info("Show found: {}", show);
        } else {
            logger.warn("Show with ID {} not found", showId);
        }
        return show;
    }

    // Update a show by its ID
    @PutMapping("/shows/{showId}")
    public Show updateShow(@PathVariable Long showId, @RequestBody Show show) {
        logger.info("Updating show with ID: {}", showId);
        Show updatedShow = service.updateShow(showId, show);
        logger.info("Show updated successfully: {}", updatedShow);
        return updatedShow;
    }

    // Delete a show by its ID
    @DeleteMapping("/shows/{showId}")
    public void deleteShow(@PathVariable Long showId) {
        logger.info("Deleting show with ID: {}", showId);
        service.deleteShow(showId);
        logger.info("Show with ID {} deleted successfully", showId);
    }
}
