package com.candidature.candidature.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {
    String smtpHost = "smtp.gmail.com";
    String smtpPort = "587";  // Port 587 pour TLS
    String smtpUsername = "gats.gatsmapping@gmail.com";
    String smtpPassword = "bula axpo ulhk vyhx";

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(smtpHost);
        mailSender.setPort(Integer.parseInt(smtpPort));
        mailSender.setUsername(smtpUsername);
        mailSender.setPassword(smtpPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");  // Activer STARTTLS
        props.put("mail.smtp.ssl.enable", "false");  // Désactiver SSL car on utilise TLS
        props.put("mail.debug", "true");

        return mailSender;
    }
}


