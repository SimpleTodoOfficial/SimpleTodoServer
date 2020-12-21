package de.calltopower.simpletodo.impl.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import de.calltopower.simpletodo.api.properties.STDProperties;
import lombok.Getter;

/**
 * Property mapping for prefix "simpletodo"
 */
@Getter
@Configuration
@ConfigurationProperties(prefix = STDSettingsProperties.PREFIX)
public class STDSettingsProperties implements STDProperties {

    /**
     * prefix in the application.properties
     */
    public static final String PREFIX = "simpletodo";

    public static final long DUE_TODOS_MINUTES_PLUS_DEFAULT = 300;

    public static final int MAX_STRING_FILE_CACHE_DEFAULT = 10;

    @Value("${" + PREFIX + ".signup}")
    private String signup;

    @Value("${" + PREFIX + ".url}")
    private String url;

    @Value("${" + PREFIX + ".url-password-reset}")
    private String urlPasswordReset;

    @Value("${" + PREFIX + ".url-password-reset-success}")
    private String urlPasswordResetSuccess;

    @Value("${" + PREFIX + ".url-user-verification}")
    private String urlUserVerification;

    @Value("${" + PREFIX + ".mail-from}")
    private String mailFrom;

    @Value("${" + PREFIX + ".duetodos-minutes-plus}")
    private Long dueTodosMinutesPlus;

    @Value("${" + PREFIX + ".cache.file.string.max}")
    private Integer maxStringFileCacheSize;

    @Override
    public String getPrefix() {
        return PREFIX;
    }

    /**
     * Returns whether signing up for new users is allowed
     * 
     * @return boolean flag whether signing up for new users is allowed
     */
    public boolean signupAllowed() {
        return !signup.equals("DISABLED");
    }

    /**
     * Returns the due Todos minutes plus value
     * 
     * @return due Todos minutes plus
     */
    public long getDueTodosMinutesPlus() {
        return dueTodosMinutesPlus == null ? DUE_TODOS_MINUTES_PLUS_DEFAULT : dueTodosMinutesPlus;
    }

    /**
     * Returns the max string file cache size
     * 
     * @return max string file cache size
     */
    public int getMaxStringFileCacheSize() {
        return maxStringFileCacheSize == null ? MAX_STRING_FILE_CACHE_DEFAULT : maxStringFileCacheSize;
    }

}
