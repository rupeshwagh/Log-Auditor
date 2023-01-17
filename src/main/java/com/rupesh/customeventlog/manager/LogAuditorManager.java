package com.rupesh.customeventlog.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rupesh.customeventlog.configuration.ApplicationData;
import com.rupesh.customeventlog.model.Context;
import com.rupesh.customeventlog.model.Event;
import com.rupesh.customeventlog.model.State;
import com.rupesh.customeventlog.repository.AlertRepository;
import com.rupesh.customeventlog.model.Alert;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

@Component
public class LogAuditorManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogAuditorManager.class);

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private ApplicationData applicationData;
    
    private long getEventExecutionTime(Event initialEvent, Event finalEvent) {
        Event endEvent = Stream.of(initialEvent, finalEvent).filter(e -> State.FINISHED.equals(e.getState())).findFirst().orElse(null);
        Event startEvent = Stream.of(initialEvent, finalEvent).filter(e -> State.STARTED.equals(e.getState())).findFirst().orElse(null);

        return Objects.requireNonNull(endEvent).getTimestamp() - Objects.requireNonNull(startEvent).getTimestamp();
    }
    
    private void preserveAlerts(Collection<Alert> alerts) {
        LOGGER.debug("Preserving alert...", alerts.size());
        alertRepository.saveAll(alerts);
    }

    public void readAndPreserveEvent(Context context) {
     
    	Map<String, Event> eventMap = new HashMap<>();

        Map<String, Alert> alerts = new HashMap<>();

        LOGGER.info("Inside readAndPreserveEvent() Reading events and perseverving alerts...");
        try (LineIterator lineIterator = FileUtils.lineIterator(new ClassPathResource("testlogfile/" + context.getLogFilePath()).getFile())) {
            String line = null;
            while (lineIterator.hasNext()) {
                Event event;
                try {
                    event = new ObjectMapper().readValue(lineIterator.nextLine(), Event.class);
                    LOGGER.trace("{}", event);

                    if (eventMap.containsKey(event.getId())) {
                        Event e1 = eventMap.get(event.getId());
                        long executionTime = getEventExecutionTime(event, e1);

                        Alert alert = new Alert(event, Math.toIntExact(executionTime));

                        if (executionTime > applicationData.getAlertThresholdMs()) {
                            alert.setAlert(Boolean.TRUE);
                            LOGGER.trace("Execution time in ms of an Event", event.getId(), executionTime);
                        }

                        alerts.put(event.getId(), alert);
                        eventMap.remove(event.getId());
                    } else {
                        eventMap.put(event.getId(), event);
                    }
                } catch (JsonProcessingException e) {
                    LOGGER.error("Not able to read event ", e.getMessage());
                }

                if (alerts.size() > applicationData.getTableRowsWriteoffCount()) {
                	preserveAlerts(alerts.values());
                    alerts = new HashMap<>();
                }
            }
            if (alerts.size() != 0) {
            	preserveAlerts(alerts.values());
            }
        } catch (IOException e) {
            LOGGER.error("Not able to access the file ", e.getMessage());
        }
    }

}
