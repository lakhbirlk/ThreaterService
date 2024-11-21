package org.theatremanagement.screen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.theatremanagement.screen.model.Screen;
import org.theatremanagement.screen.model.ScreenChart;
@Repository
public interface ScreenChartRepository extends JpaRepository<ScreenChart, Long> {

}
