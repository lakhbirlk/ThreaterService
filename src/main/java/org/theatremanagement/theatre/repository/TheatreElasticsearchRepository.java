package org.theatremanagement.theatre.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.theatremanagement.theatre.model.TheatreDoc;

import java.util.List;

public interface TheatreElasticsearchRepository extends ElasticsearchRepository<TheatreDoc, Long> {
    List<TheatreDoc> findByNameContaining(String name);
    List<TheatreDoc> findByLocationContaining(String location);
}
