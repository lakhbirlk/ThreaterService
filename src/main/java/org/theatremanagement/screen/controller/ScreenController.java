package org.theatremanagement.screen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.theatremanagement.screen.model.Screen;
import org.theatremanagement.screen.service.ScreenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

// screenController.java
@RestController
@RequestMapping("/api/v1/screens")
public class ScreenController {

    private static final Logger logger = LoggerFactory.getLogger(ScreenController.class);

    @Autowired
    private ScreenService service;

    @PostMapping
    public Screen addscreen(@RequestBody Screen screen) {
        logger.info("Adding new screen: {}", screen); // Log the screen being added
        Screen addedScreen = service.addscreen(screen);
        logger.info("Screen added successfully: {}", addedScreen); // Log the added screen
        return addedScreen;
    }

    @GetMapping("/{screenId}")
    public Screen getscreen(@PathVariable Long screenId) {
        logger.info("Fetching screen with ID: {}", screenId); // Log the request to fetch the screen
        Screen screen = service.getscreen(screenId);
        if (screen != null) {
            logger.info("Screen found: {}", screen); // Log the fetched screen
        } else {
            logger.warn("Screen with ID {} not found", screenId); // Log a warning if not found
        }
        return screen;
    }

    @PutMapping("/{screenId}")
    public Screen updatescreen(@PathVariable Long screenId, @RequestBody Screen screen) {
        logger.info("Updating screen with ID: {}", screenId); // Log the update request
        Screen updatedScreen = service.updatescreen(screenId, screen);
        logger.info("Screen updated successfully: {}", updatedScreen); // Log the updated screen
        return updatedScreen;
    }

    @DeleteMapping("/{screenId}")
    public void deleteScreen(@PathVariable Long screenId) {
        logger.info("Deleting screen with ID: {}", screenId); // Log the delete request
        service.deleteScreen(screenId);
        logger.info("Screen with ID {} deleted successfully", screenId); // Log successful deletion
    }

    @GetMapping("/search")
    public List<Screen> searchByName(@RequestParam String name) {
        logger.info("Searching screens by name: {}", name); // Log the search request
        List<Screen> screens = service.searchByName(name);
        if (screens.isEmpty()) {
            logger.warn("No screens found with name: {}", name); // Log a warning if no screens found
        } else {
            logger.info("Found {} screens with name: {}", screens.size(), name); // Log the result of search
        }
        return screens;
    }
}
