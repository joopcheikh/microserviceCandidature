package com.candidature.candidature.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import java.util.Properties;

/**
 * @author cheikh diop
 *
 * Configuration de l'envoi d'emails pour l'application.
 * Cette classe définit un bean pour le service d'envoi d'emails via SMTP
 * en utilisant Spring's JavaMailSender.
 */
@Configuration
public class MailConfig {

    /**
     * Crée un bean de JavaMailSender pour la configuration de l'envoi d'emails.
     *
     * @return JavaMailSender configuré pour l'envoi d'emails
     */
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // Configuration de l'hôte SMTP
        mailSender.setHost("mail.gatsmapping.com"); // Remplacez par l'hôte SMTP réel
        mailSender.setPort(465); // Port pour la connexion SSL

        // Authentification
        mailSender.setUsername("webmaster@gatsmapping.com"); // Votre adresse email
        mailSender.setPassword("vPORL@h+VV_0"); // Mot de passe de l'email

        // Propriétés supplémentaires pour la configuration de l'envoi d'emails
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp"); // Protocole de transport
        props.put("mail.smtp.auth", "true"); // Authentification requise
        props.put("mail.smtp.ssl.enable", "true"); // Activer SSL pour la connexion sécurisée
        props.put("mail.debug", "true"); // Active le mode debug pour les logs d'envoi

        return mailSender; // Retourne l'instance de JavaMailSender configurée
    }
}
