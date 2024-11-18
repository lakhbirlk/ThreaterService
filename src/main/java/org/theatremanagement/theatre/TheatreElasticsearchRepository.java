package org.theatremanagement.theatre;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface TheatreElasticsearchRepository extends ElasticsearchRepository<TheatreDoc, Long> {
    List<TheatreDoc> findByNameContaining(String name);
    List<TheatreDoc> findByLocationContaining(String location);
}
