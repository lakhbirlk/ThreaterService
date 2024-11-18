package org.theatremanagement.theatre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.theatremanagement.exception.ResourceNotFoundException;
import java.util.List;

@Service
public  class TheatreService{

    @Autowired
    private TheatreRepository theatreRepository;


    @Autowired
    private TheatreElasticsearchRepository theatreElasticsearchRepository;


    /**
     * Add a new theatre
     *
     * @param theatre Theatre entity
     * @return The saved Theatre entity
     */
    public Theatre addTheatre(Theatre theatre) {
        theatreRepository.save(theatre); // Save to DB using JPA
        theatreElasticsearchRepository.save(org.theatremanagement.theatre.mapper.TheatreToTheatreDocMapper.toTheatreDoc(theatre)); // Save to Elasticsearch
        return theatre;
    }/**
     * Retrieve all theatres
     *
     * @return List of theatres
     */
    public List<Theatre> getAllTheatres() {
     return   theatreRepository.findAll(PageRequest.of(0, 10)).getContent();
        //return docs.stream().map(org.theatremanagement.theatre.mapper.TheatreToTheatreDocMapper::toTheatre).toList();
    }

/**
 * Retrieve a specific theatre by ID
 *
 * @param theatreId Theatre ID
 * @return The Theatre entity
 * @throws ResourceNotFoundException if the theatre does not exist
 */
public Theatre getTheatre(Long theatreId) {
    return theatreRepository.findById(theatreId)
            .orElseThrow(() -> new ResourceNotFoundException("Theatre not found with id: " + theatreId));
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
    Theatre existingTheatre = theatreRepository.findById(theatreId)
            .orElseThrow(() -> new ResourceNotFoundException("Theatre not found with id: " + theatreId));

    existingTheatre.setName(updatedTheatre.getName());
    existingTheatre.setLocation(updatedTheatre.getLocation());
    //theatreElasticsearchRepository.deleteById(existingTheatre.getId());
    //theatreElasticsearchRepository.save(org.theatremanagement.theatre.mapper.TheatreToTheatreDocMapper.toTheatreDoc(existingTheatre));
    return theatreRepository.save(existingTheatre);
}

/**
 * Delete a theatre by ID
 *
 * @param theatreId Theatre ID
 * @throws ResourceNotFoundException if the theatre does not exist
 */
public void deleteTheatre(Long theatreId) {
    if (!theatreRepository.existsById(theatreId)) {
        throw new ResourceNotFoundException("Theatre not found with id: " + theatreId);
    }
    theatreRepository.deleteById(theatreId);
}

public List<Theatre> searchByName(String name) {
    return theatreElasticsearchRepository.findByNameContaining(name).stream().map(org.theatremanagement.theatre.mapper.TheatreToTheatreDocMapper::toTheatre).toList(); // Search in Elasticsearch
}

public List<Theatre> searchByLocation(String location) {
    return theatreElasticsearchRepository.findByLocationContaining(location).stream().map(org.theatremanagement.theatre.mapper.TheatreToTheatreDocMapper::toTheatre).toList(); // Search in Elasticsearch
}

}
