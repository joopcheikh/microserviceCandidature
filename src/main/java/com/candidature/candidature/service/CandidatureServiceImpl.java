package com.candidature.candidature.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.candidature.candidature.model.Candidature;
import com.candidature.candidature.model.Status;
import com.candidature.candidature.repository.CandidatureRepository;

@Service
public class CandidatureServiceImpl {

    @Autowired
    private CandidatureRepository candidatureRepository;

    @Autowired
    private EmailService emailService;

    
    public List<Candidature> listerCandidature() {
        return candidatureRepository.findAll();
    }


    public void rejeterCandidature(Integer candidatureId) {
        Candidature candidature = candidatureRepository.findById(candidatureId)
                .orElseThrow(() -> new RuntimeException("Candidature non trouvée"));
        
        candidature.setStatus(Status.REJECTED);
        candidatureRepository.save(candidature);
        
        String userEmail = candidature.getUser().getEmail(); // Utiliser le user_id pour retrouver l'email
        String subject = "Candidature Rejetée";
        String content = "Votre candidature pour " + candidature.getConcours() + " a été rejetée.";
        emailService.sendEmail(userEmail, subject, content);
    }

   
    public void acceptCandidature(Integer candidatureId) {
        Candidature candidature = candidatureRepository.findById(candidatureId)
                .orElseThrow(() -> new RuntimeException("Candidature non trouvée"));
        
        candidature.setStatus(Status.ACCEPTED);
        candidatureRepository.save(candidature);

        String userEmail = candidature.getUser().getEmail(); // Utiliser le user_id pour retrouver l'email
        String subject = "Candidature Acceptée";
        String content = "Félicitations! Votre candidature pour " + candidature.getConcours() + " a été acceptée.";
        emailService.sendEmail(userEmail, subject, content);
    }

    public void awaitCandidature(Integer candidatureId) {
        Candidature candidature = candidatureRepository.findById(candidatureId)
                .orElseThrow(() -> new RuntimeException("Candidature non trouvée"));
        
        candidature.setStatus(Status.WAITING);
        candidatureRepository.save(candidature);
        
        String userEmail = candidature.getUser().getEmail(); // Utiliser le user_id pour retrouver l'email
        String subject = "Candidature en Attente";
        String content = "Votre candidature pour " + candidature.getConcours() + " est en attente.";
        emailService.sendEmail(userEmail, subject, content);
    }
}
