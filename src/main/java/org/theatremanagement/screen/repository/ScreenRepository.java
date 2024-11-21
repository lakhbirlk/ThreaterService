package org.theatremanagement.screen.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import org.theatremanagement.screen.model.Screen;

@Repository
public interface ScreenRepository extends JpaRepository<Screen, Long> {

}
