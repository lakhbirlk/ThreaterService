package org.theatremanagement.screen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.theatremanagement.screen.model.Screen;
import org.theatremanagement.screen.service.ScreenService;

import java.util.List;


// screenController.java
@RestController
@RequestMapping("/api/v1/screens")
public class ScreenController {
    @Autowired
    private ScreenService service;

    @PostMapping
    public Screen addscreen(@RequestBody Screen screen) {
        return service.addscreen(screen);
    }

    @GetMapping("/{screenId}")
    public Screen getscreen(@PathVariable Long screenId) {
        return service.getscreen(screenId);
    }

    @PutMapping("/{screenId}")
    public Screen updatescreen(@PathVariable Long screenId, @RequestBody Screen screen) {
        return service.updatescreen(screenId, screen);
    }
    @DeleteMapping("/{screenId}")
    public void deleteScreen(@PathVariable Long screenId) {
        service.deleteScreen(screenId);
    }
    @GetMapping("/search")
    public List<Screen> searchByName(@RequestParam String name) {
        List<Screen> screens = service.searchByName(name);
        return screens;
    }


}
