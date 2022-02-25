package de.calltopower.simpletodo.impl.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import de.calltopower.simpletodo.api.service.STDService;
import de.calltopower.simpletodo.impl.model.STDUserForgotPasswordTokenModel;
import de.calltopower.simpletodo.impl.model.STDUserModel;
import de.calltopower.simpletodo.impl.model.STDUserVerificationTokenModel;
import de.calltopower.simpletodo.impl.properties.STDSettingsProperties;

@Service
public class STDEmailService implements STDService {

    private static final Logger LOGGER = LoggerFactory.getLogger(STDEmailService.class);

    private STDSettingsProperties settingsProperties;
    private JavaMailSender javaMailSender;
    private STDEmailTemplateService emailTemplateService;

    /**
     * Initializes the service
     * 
     * @param settingsProperties   Settings properties
     * @param javaMailSender       Java Mail Sender
     * @param emailTemplateService Email template service
     */
    @Autowired
    public STDEmailService(STDSettingsProperties settingsProperties, JavaMailSender javaMailSender,
            STDEmailTemplateService emailTemplateService) {
        this.settingsProperties = settingsProperties;
        this.javaMailSender = javaMailSender;
        this.emailTemplateService = emailTemplateService;
    }

    /**
     * Sends an account created email
     * 
     * @param toEmail The email address to send the email to
     * @param user    The user
     */
    public void sendAccountCreatedEmail(String toEmail, STDUserModel user) {
        LOGGER.debug("Sending account created email");

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(settingsProperties.getMailFrom());
            messageHelper.setTo(toEmail);
            messageHelper.setSubject("SimpleTodo: Account erstellt / Account created");
            String content = emailTemplateService.buildAccountCreated(user.getUsername());
            messageHelper.setText(content, true);
        };

        javaMailSender.send(messagePreparator);
    }

    /**
     * Sends a forgot password email
     * 
     * @param toEmail The email address to send the email to
     * @param token   The forgot password token
     */
    public void sendPasswordForgotEmail(String toEmail, STDUserForgotPasswordTokenModel model) {
        LOGGER.debug("Sending forgot password email");

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(settingsProperties.getMailFrom());
            messageHelper.setTo(toEmail);
            messageHelper
                    .setSubject("SimpleTodo: Passwort-Zurücksetzen-Token generiert / Password Reset Token generated");
            String content = emailTemplateService.buildForgotPassword(model.getId().toString());
            messageHelper.setText(content, true);
        };

        javaMailSender.send(messagePreparator);
    }

    /**
     * Sends a new password generated email
     * 
     * @param toEmail     The email address to send the email to
     * @param newPassword The new password
     */
    public void sendNewPasswordGeneratedEmail(String toEmail, String newPassword) {
        LOGGER.debug("Sending new password generated email");

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(settingsProperties.getMailFrom());
            messageHelper.setTo(toEmail);
            messageHelper.setSubject("SimpleTodo: Neues Passwort generiert / New Password generated");
            String content = emailTemplateService.buildNewPasswordGenerated(newPassword);
            messageHelper.setText(content, true);
        };

        javaMailSender.send(messagePreparator);
    }

    /**
     * Sends an email address verification email
     * 
     * @param toEmail The email address to send the email to
     * @param user    The user
     * @param model   The verify email address token
     */
    public void sendVerifyEmailAddressEmail(String toEmail, STDUserModel user, STDUserVerificationTokenModel model) {
        LOGGER.debug("Sending verify email address email");

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(settingsProperties.getMailFrom());
            messageHelper.setTo(toEmail);
            messageHelper.setSubject("SimpleTodo: Verifiziere deine E-Mail-Adresse / Verify your email address");
            String content = emailTemplateService.buildVerifyEmailAddress(user.getId().toString(),
                    model.getId().toString());
            messageHelper.setText(content, true);
            LOGGER.info(content);
        };

        javaMailSender.send(messagePreparator);
    }

    /**
     * Sends an email address verified email
     * 
     * @param toEmail The email address to send the email to
     */
    public void sendEmailAddressverifiedEmail(String toEmail) {
        LOGGER.debug("Sending email address verified email");

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(settingsProperties.getMailFrom());
            messageHelper.setTo(toEmail);
            messageHelper.setSubject("SimpleTodo: E-Mail-Adresse verifiziert / Email address verified");
            String content = emailTemplateService.buildEmailAddressVerified();
            messageHelper.setText(content, true);
        };

        javaMailSender.send(messagePreparator);
    }

    /**
     * Sends a plain text email
     * 
     * @param toEmail The email address to send the email to
     * @param subject The subject
     * @param message The message
     */
    public void sendEmail(String toEmail, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(settingsProperties.getMailFrom());
        mailMessage.setTo(toEmail);
        mailMessage.setSubject(String.format("SimpleTodo: %s", subject));
        mailMessage.setText(message);

        javaMailSender.send(mailMessage);
    }

}
