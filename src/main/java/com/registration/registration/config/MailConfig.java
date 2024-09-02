package com.registration.registration.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.example.com"); // Remplacez par l'hôte SMTP réel
        mailSender.setPort(587); // Port SMTP, habituellement 587 pour TLS

        mailSender.setUsername("your-email@example.com"); // Votre adresse email
        mailSender.setPassword("your-email-password"); // Mot de passe de l'email

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true"); // Active le mode debug pour les logs

        return mailSender;
    }
}
