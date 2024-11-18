package org.theatremanagement.theatre;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.theatremanagement.theatre.Theatre;

import java.util.List;

@RestController
@RequestMapping("/api/v1/theatres")
public class TheatreController {

    private final TheatreService theatreService;

    public TheatreController(TheatreService theatreService) {
        this.theatreService = theatreService;
    }

    @PostMapping
    public ResponseEntity<Theatre> createTheatre(@RequestBody Theatre theatre) {
        return new ResponseEntity<>(theatreService.addTheatre(theatre), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Theatre>> getAllTheatres() {
        return new ResponseEntity<>(theatreService.getAllTheatres(), HttpStatus.OK);
    }

    @GetMapping("/{theatreId}")
    public ResponseEntity<Theatre> getTheatre(@PathVariable Long theatreId) {
        return new ResponseEntity<>(theatreService.getTheatre(theatreId), HttpStatus.OK);
    }

    @PutMapping("/{theatreId}")
    public ResponseEntity<Theatre> updateTheatre(@PathVariable Long theatreId, @RequestBody Theatre theatre) {
        return new ResponseEntity<>(theatreService.updateTheatre(theatreId, theatre), HttpStatus.OK);
    }

    @DeleteMapping("/{theatreId}")
    public ResponseEntity<Void> deleteTheatre(@PathVariable Long theatreId) {
        theatreService.deleteTheatre(theatreId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/search")
    public List<Theatre> searchTheatres(@RequestParam String name) {
        return theatreService.searchByName(name);
    }

    @GetMapping("/search/location")
    public List<Theatre> searchByLocation(@RequestParam String location) {
        return theatreService.searchByLocation(location);
    }
}
