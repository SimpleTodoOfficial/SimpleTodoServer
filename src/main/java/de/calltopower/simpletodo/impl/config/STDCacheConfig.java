package de.calltopower.simpletodo.impl.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

import de.calltopower.simpletodo.api.config.STDConfig;

/**
 * Cache configuration
 */
@Configuration
@EnableCaching
public class STDCacheConfig implements STDConfig {
    // Nothing to see here...
}
