package de.calltopower.simpletodo.impl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import de.calltopower.simpletodo.api.service.STDService;
import de.calltopower.simpletodo.impl.properties.STDSettingsProperties;

@Service
public class STDEmailService implements STDService {

    private STDSettingsProperties settingsProperties;
    private JavaMailSender javaMailSender;

    @Autowired
    public STDEmailService(STDSettingsProperties settingsProperties, JavaMailSender javaMailSender) {
        this.settingsProperties = settingsProperties;
        this.javaMailSender = javaMailSender;
    }

    public void sendMail(String toEmail, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setSubject(String.format("SimpleTodo: %s", subject));
        mailMessage.setText(message);
        mailMessage.setFrom(settingsProperties.getMailFrom());

        javaMailSender.send(mailMessage);
    }

}
