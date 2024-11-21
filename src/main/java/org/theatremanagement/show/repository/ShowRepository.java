package org.theatremanagement.show.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.theatremanagement.show.model.Show;


import java.util.List;
@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {
    List<Show> findByscreenId(Long screenId);
}
