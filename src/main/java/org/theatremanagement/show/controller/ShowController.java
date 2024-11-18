package org.theatremanagement.show.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import org.theatremanagement.show.model.Show;
import org.theatremanagement.show.service.ShowService;


import java.util.List;

// ShowController.java
@RestController
@RequestMapping("/api/v1")
public class ShowController {
    @Autowired
    private ShowService service;

    @PostMapping("/screens/{screenId}/shows")
    public Show addShow(@PathVariable Long screenId, @RequestBody Show show) {
        return service.addShow(screenId, show);
    }

    @GetMapping("/screens/{screenId}/shows")
    public List<Show> getShowsByscreen(@PathVariable Long screenId) {
        return service.getShowsByScreen(screenId);
    }

    @GetMapping("/shows/{showId}")
    public Show getShow(@PathVariable Long showId) {
        return service.getShow(showId);
    }

    @PutMapping("/shows/{showId}")
    public Show updateShow(@PathVariable Long showId, @RequestBody Show show) {
        return service.updateShow(showId, show);
    }

    @DeleteMapping("/shows/{showId}")
    public void deleteShow(@PathVariable Long showId) {
        service.deleteShow(showId);
    }
}
