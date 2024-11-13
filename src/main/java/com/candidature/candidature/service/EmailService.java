package com.candidature.candidature.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/**
 * @author cheikh diop, sosthene
 *
 * Service `EmailService` pour l'envoi d'e-mails dans l'application.
 * Cette classe utilise `JavaMailSender` pour envoyer des e-mails au format MIME.
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Envoie un e-mail au destinataire spécifié avec un sujet et un contenu.
     *
     * @param to      L'adresse e-mail du destinataire.
     * @param subject Le sujet de l'e-mail.
     * @param content Le contenu de l'e-mail, qui peut être au format HTML si `isHtml` est vrai.
     * @throws RuntimeException Si une erreur se produit lors de l'envoi de l'e-mail.
     */
    public void sendEmail(String to, String subject, String content) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email: " + e.getMessage());
        }
    }
}
