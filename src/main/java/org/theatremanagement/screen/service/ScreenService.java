package org.theatremanagement.screen.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.RefreshPolicy;
import org.springframework.stereotype.Service;
import org.theatremanagement.screen.mapper.ScreenToScreenDocMapper;
import org.theatremanagement.screen.model.Screen;
import org.theatremanagement.screen.repository.ScreenElasticsearchRepository;
import org.theatremanagement.screen.repository.ScreenRepository;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


@Service
public class ScreenService {
    @Autowired
    private ScreenRepository repository;

    @Autowired
    private ScreenElasticsearchRepository screenElasticsearchRepository; // Elasticsearch Repository

    public Screen addscreen(Screen screen) {
        return repository.save(screen);
    }

    public Screen getscreen(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("screen not found"));
    }

    public Screen updatescreen(Long id, Screen updatedscreen) {
        Screen screen = getscreen(id);
        screen.setName(updatedscreen.getName());
        screen.setSeatingCapacity(updatedscreen.getSeatingCapacity());
        ScreenToScreenDocMapper.toScreenDoc(screen);
        return repository.save(screen);
    }

    public void deleteScreen(Long id){
        repository.deleteById(id);
        screenElasticsearchRepository.deleteById(id);
    }

    public Screen saveScreen(Screen screen) {
        repository.save(screen);
        screenElasticsearchRepository.save(ScreenToScreenDocMapper.toScreenDoc(screen));
         // Save to Elasticsearch
        return screen;
    }

    public List<Screen> searchByName(String name) {
    return screenElasticsearchRepository.findByNameContaining(name).stream().map(ScreenToScreenDocMapper::toScreen).toList(); // Search in Elasticsearch

    }

}
