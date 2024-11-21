package org.theatremanagement.screen.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.theatremanagement.kafka.KafkaProducerService;
import org.theatremanagement.kafka.event.ScreenChartCreatedEvent;
import org.theatremanagement.kafka.event.ShowCreatedEvent;
import org.theatremanagement.screen.model.ScreenChart;
import org.theatremanagement.screen.repository.ScreenChartRepository;
import org.theatremanagement.screen.repository.ScreenRepository;

import java.time.LocalDateTime;


@Service
public class ScreenChartService {

    @Autowired
    private ScreenChartRepository repository;

    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;



    @KafkaListener(topics = "show.created", groupId = "theatre-group")
    public void createScreenCreate(ShowCreatedEvent event) {
        // Create a predefined set of screens for the theatre
            ScreenChart screenChart = new ScreenChart();
            screenChart.setShowId(event.getShowId());
            LocalDateTime startTime=event.getStartTime();
            screenChart.setStartTime(startTime);
            screenChart.setDate(event.getStartTime());
            screenChart.setEndTime(event.getStartTime().plusMinutes(event.getLength()));
            screenChart.setScreen(screenRepository.findById(event.getScreenId()).get());
            screenChart.setShowId(event.getShowId());
            ScreenChart savedScreenChart=repository.save(screenChart);
            ScreenChartCreatedEvent screenChartCreatedEvent = new ScreenChartCreatedEvent();
        screenChartCreatedEvent.setScreenId(event.getScreenId());
        screenChartCreatedEvent.setShowId(event.getShowId());
            final String screenChartTime=startTime.toString();
        screenChartCreatedEvent.setShowSlotNumber(savedScreenChart.getId());
        screenChartCreatedEvent.setCapacity(event.getCapacity());
        screenChartCreatedEvent.setStartTime(startTime);
        screenChartCreatedEvent.setLength(event.getLength());
            kafkaProducerService.publishEvent("screenChart.created", screenChartCreatedEvent);
    }





}
