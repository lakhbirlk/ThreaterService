package org.theatremanagement.screen.repository;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.theatremanagement.screen.model.Screen;
import org.theatremanagement.screen.model.ScreenDoc;

import java.util.List;

@Repository
public interface ScreenElasticsearchRepository extends ElasticsearchRepository<ScreenDoc, Long> {
    List<ScreenDoc> findByNameContaining(String name);

}
