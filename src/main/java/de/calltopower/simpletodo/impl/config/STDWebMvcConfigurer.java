package de.calltopower.simpletodo.impl.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import de.calltopower.simpletodo.api.config.STDConfig;

/**
 * Web MVC configuration - only allow CORS-requests in dev mode
 */
@Configuration
@EnableWebMvc
@Profile("dev")
public class STDWebMvcConfigurer implements WebMvcConfigurer, STDConfig {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**").allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                                      .allowedOriginPatterns("http://localhost:[*]");
    }

}
