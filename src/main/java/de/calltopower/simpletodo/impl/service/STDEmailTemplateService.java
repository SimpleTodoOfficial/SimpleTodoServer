package de.calltopower.simpletodo.impl.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import de.calltopower.simpletodo.api.service.STDService;
import de.calltopower.simpletodo.impl.properties.STDSettingsProperties;

/**
 * Service for email templates
 */
@Service
public class STDEmailTemplateService implements STDService {

    private static final Logger LOGGER = LoggerFactory.getLogger(STDEmailTemplateService.class);

    private TemplateEngine templateEngine;
    private STDSettingsProperties settingsProperties;

    @Autowired
    public STDEmailTemplateService(TemplateEngine templateEngine, STDSettingsProperties settingsProperties) {
        this.templateEngine = templateEngine;
        this.settingsProperties = settingsProperties;
    }

    public String buildAccountCreated(String username) {
        LOGGER.debug("Building account created HTML email");

        Map<String, String> templateVariables = new HashMap<>();
        templateVariables.put("url", settingsProperties.getUrl());
        templateVariables.put("url_text", settingsProperties.getUrl());
        templateVariables.put("username", username);

        return build("account_created", templateVariables);
    }

    public String buildForgotPassword(String token) {
        LOGGER.debug("Building forgot password HTML email");

        Map<String, String> templateVariables = new HashMap<>();
        templateVariables.put("url", settingsProperties.getUrlPasswordReset());
        templateVariables.put("url_text", settingsProperties.getUrlPasswordReset());
        templateVariables.put("token", token);

        return build("forgot_password", templateVariables);
    }

    public String buildNewPasswordGenerated(String newPassword) {
        LOGGER.debug("Building new password generated HTML email");

        Map<String, String> templateVariables = new HashMap<>();
        templateVariables.put("url", settingsProperties.getUrlPasswordResetSuccess());
        templateVariables.put("url_text", settingsProperties.getUrlPasswordResetSuccess());
        templateVariables.put("password", newPassword);

        return build("new_password_generated", templateVariables);
    }

    public String buildVerifyEmailAddress(String userId, String token) {
        LOGGER.debug("Building verify email address HTML email");

        Map<String, String> templateVariables = new HashMap<>();
        String url = String.format(settingsProperties.getUrlUserVerification(), userId);
        templateVariables.put("url", url);
        templateVariables.put("url_text", url);
        templateVariables.put("token", token);

        return build("verify_email_address", templateVariables);
    }

    public String buildEmailAddressVerified() {
        LOGGER.debug("Building email address verified HTML email");

        Map<String, String> templateVariables = new HashMap<>();

        return build("email_address_verified", templateVariables);
    }

    private String build(String template, Map<String, String> templateVariables) {
        Context context = new Context();
        for (Map.Entry<String, String> entry : templateVariables.entrySet()) {
            context.setVariable(entry.getKey(), entry.getValue());
        }

        return templateEngine.process(template, context);
    }

}
