package de.calltopower.simpletodo.impl.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import de.calltopower.simpletodo.api.task.STDSchedulingTask;

/**
 * A monitoring background task
 */
@Configuration
public class STDMonitoringSchedulingTask implements STDSchedulingTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(STDMonitoringSchedulingTask.class);
    // private static final SimpleDateFormat dateFormat = new
    // SimpleDateFormat("yyyy.MM.dd-HH:mm:ss");

    // private final long INITIAL_DELAY_UPDATE_MILLIS = 10000;
    // private final long FIXED_RATE_UPDATE_MILLIS = 10 * 30 * 1000;

    /**
     * Initializes the configuration. Loads the environments and caches the names.
     * 
     * @param monitoringProperties Injected monitoring properties
     * @param monitorFactories     Injected monitor factories
     */
    @Autowired
    public STDMonitoringSchedulingTask() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Starting tasks");
        }
    }

    /**
     * Updates periodically.
     */
    // @formatter:off
    /*
    @Scheduled(initialDelay = INITIAL_DELAY_UPDATE_MILLIS, fixedRate = FIXED_RATE_UPDATE_MILLIS)
    public void update() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Updating");
        }
    }
    */
    // @formatter:on

}
