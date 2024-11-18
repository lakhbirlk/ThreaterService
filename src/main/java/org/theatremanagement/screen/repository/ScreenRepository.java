package org.theatremanagement.screen.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.theatremanagement.screen.model.Screen;

// screenRepository.java
public interface ScreenRepository extends JpaRepository<Screen, Long> {

}
