package org.theatremanagement.show.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import org.theatremanagement.screen.model.Screen;
import org.theatremanagement.show.mapper.ShowToShowDocMapper;
import org.theatremanagement.show.model.Show;
import org.theatremanagement.show.repository.ShowElasticsearchRepository;
import org.theatremanagement.show.repository.ShowRepository;

import java.util.List;

// ShowService.java
@Service
public class ShowService {
    @Autowired
    public ShowRepository repository;

    @Autowired
    private ShowElasticsearchRepository showElasticsearchRepository; // Elasticsearch Repository


    public Show addShow(Long screenId, Show show) {
        Screen screen = new Screen();
        screen.setId(screenId);
        show.setScreen(screen);
        return repository.save(show);
    }

    public Show saveShow(Show show) {
        repository.save(show); // Save to DB using JPA
        showElasticsearchRepository.save(ShowToShowDocMapper.toShowDoc(show)); // Save to Elasticsearch
        return show;
    }

    public List<Show> searchByName(String title) {
        return showElasticsearchRepository.findByTitleContaining(title).stream().map(ShowToShowDocMapper::toShow).toList(); // Search in Elasticsearch
    }

    public List<Show> searchByShowTime(String showTime) {
        return showElasticsearchRepository.findByShowTime(showTime).stream().map(ShowToShowDocMapper::toShow).toList(); // Search in Elasticsearch
    }
    public List<Show> getShowsByScreen(Long screenId) {
        return repository.findByscreenId(screenId);
    }

    public Show getShow(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Show not found"));
    }

    public Show updateShow(Long id, Show updatedShow) {
        Show show = getShow(id);
        show.setTitle(updatedShow.getTitle());
        show.setShowTime(updatedShow.getShowTime());
        showElasticsearchRepository.save(ShowToShowDocMapper.toShowDoc(show));
        return repository.save(show);
    }

    public void deleteShow(Long id) {
        showElasticsearchRepository.deleteById(id);
        repository.deleteById(id);
    }
}
