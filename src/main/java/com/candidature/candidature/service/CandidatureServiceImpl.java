package com.candidature.candidature.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.candidature.candidature.model.Candidature;
import com.candidature.candidature.model.Status;
import com.candidature.candidature.repository.CandidatureRepository;

@Service
public class CandidatureServiceImpl {

    @Autowired
    private CandidatureRepository candidatureRepository;

    @Autowired
    private JavaMailSender mailSender;

    public List<Candidature> listerCandidature() {
        return candidatureRepository.findAll();
    }

    public void rejeterCandidature(Integer candidatureId) {
        updateCandidatureStatusAndNotify(candidatureId, Status.REJECTED, 
            "Candidature Rejetée", 
            "Votre candidature pour %s a été rejetée.");
    }

    public void acceptCandidature(Integer candidatureId) {
        updateCandidatureStatusAndNotify(candidatureId, Status.ACCEPTED, 
            "Candidature Acceptée", 
            "Félicitations! Votre candidature pour %s a été acceptée.");
    }

    public void awaitCandidature(Integer candidatureId) {
        updateCandidatureStatusAndNotify(candidatureId, Status.WAITING, 
            "Candidature en Attente", 
            "Votre candidature pour %s est en attente.");
    }

    private void updateCandidatureStatusAndNotify(Integer candidatureId, Status status, String subject, String contentTemplate) {
        Candidature candidature = candidatureRepository.findById(candidatureId)
                .orElseThrow(() -> new RuntimeException("Candidature non trouvée"));

        candidature.setStatus(status);
        candidatureRepository.save(candidature);

        String userEmail = candidature.getUser().getEmail(); // Utiliser le user_id pour retrouver l'email
        String content = String.format(contentTemplate, candidature.getConcours());

        sendEmail(userEmail, subject, content);
    }

    private void sendEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("webmaster@gatsmapping.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }
}
