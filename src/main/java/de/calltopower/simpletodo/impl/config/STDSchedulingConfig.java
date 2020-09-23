package de.calltopower.simpletodo.impl.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import de.calltopower.simpletodo.api.config.STDConfig;

/**
 * Scheduling configuration
 */
@Configuration
@EnableScheduling
public class STDSchedulingConfig implements STDConfig {
    // Nothing to see here...
}
