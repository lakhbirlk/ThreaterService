package org.theatremanagement.theatre.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.theatremanagement.theatre.model.Theatre;

@Repository
public interface TheatreRepository extends JpaRepository<Theatre, Long> {
}
