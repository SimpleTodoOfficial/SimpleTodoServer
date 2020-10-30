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

    @Override
    public String getPrefix() {
        return PREFIX;
    }

    public boolean signupAllowed() {
        return !signup.equals("DISABLED");
    }

    public long getDueTodosMinutesPlus() {
        return dueTodosMinutesPlus == null ? DUE_TODOS_MINUTES_PLUS_DEFAULT : dueTodosMinutesPlus;
    }

}
