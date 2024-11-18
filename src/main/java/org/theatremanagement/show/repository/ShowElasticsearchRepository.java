package org.theatremanagement.show.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.theatremanagement.show.model.Show;
import org.theatremanagement.show.model.ShowDoc;

import java.util.List;

@Repository
public interface ShowElasticsearchRepository extends ElasticsearchRepository<ShowDoc, Long> {
    List<ShowDoc> findByTitleContaining(String title);
    List<ShowDoc> findByShowTime(String showTime);
}
