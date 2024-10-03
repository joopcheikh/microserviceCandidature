package com.candidature.candidature.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.candidature.candidature.model.Candidature;
import com.candidature.candidature.model.Status;
import com.candidature.candidature.repository.CandidatureRepository;

/**
 * @author cheikh diop, sosthene
 *
 * La classe `CandidatureServiceImpl` gère les opérations liées aux candidatures,
 * y compris la création, l'acceptation, le rejet et la mise à jour des candidatures.
 * Elle envoie également des notifications par email aux utilisateurs concernant le statut
 * de leur candidature.
 */
@Service
public class CandidatureServiceImpl {

    @Autowired
    private CandidatureRepository candidatureRepository;

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Récupère la liste de toutes les candidatures.
     *
     * @return Une liste de toutes les candidatures.
     */
    public List<Candidature> listerCandidature() {
        return candidatureRepository.findAll();
    }

    /**
     * Récupère les candidatures d'un utilisateur spécifique par son ID.
     *
     * @param userId L'ID de l'utilisateur.
     * @return Une liste de candidatures associées à cet utilisateur.
     */
    public List<Candidature> getConcoursByUserId(Integer userId) {
        return candidatureRepository.findByUserId(userId);
    }

    /**
     * Rejette une candidature et notifie l'utilisateur par email.
     *
     * @param candidatureId L'ID de la candidature à rejeter.
     */
    public void rejeterCandidature(Integer candidatureId) {
        updateCandidatureStatusAndNotify(candidatureId, Status.REJECTED,
                "Candidature Rejetée",
                "Votre candidature pour %s a été rejetée.");
    }

    /**
     * Accepte une candidature et notifie l'utilisateur par email.
     *
     * @param candidatureId L'ID de la candidature à accepter.
     */
    public void acceptCandidature(Integer candidatureId) {
        updateCandidatureStatusAndNotify(candidatureId, Status.ACCEPTED,
                "Candidature Acceptée",
                "Félicitations! Votre candidature pour %s a été acceptée.");
    }

    /**
     * Met à jour une candidature pour qu'elle soit en attente et notifie l'utilisateur par email.
     *
     * @param candidatureId L'ID de la candidature à mettre en attente.
     */
    public void awaitCandidature(Integer candidatureId) {
        updateCandidatureStatusAndNotify(candidatureId, Status.WAITING,
                "Candidature en Attente",
                "Votre candidature pour %s est en attente.");
    }

    /**
     * Met à jour le statut d'une candidature et envoie un email de notification à l'utilisateur.
     *
     * @param candidatureId L'ID de la candidature à mettre à jour.
     * @param status Le nouveau statut de la candidature.
     * @param subject Le sujet de l'email.
     * @param contentTemplate Le modèle de contenu de l'email.
     */
    private void updateCandidatureStatusAndNotify(Integer candidatureId, Status status, String subject, String contentTemplate) {
        Candidature candidature = candidatureRepository.findById(candidatureId)
                .orElseThrow(() -> new RuntimeException("Candidature non trouvée"));

        candidature.setStatus(status);
        candidatureRepository.save(candidature);

        String userEmail = candidature.getUser().getEmail(); // Utiliser le user_id pour retrouver l'email
        String content = String.format(contentTemplate, candidature.getConcours());

        sendEmail(userEmail, subject, content);
    }

    /**
     * Envoie un email à l'utilisateur avec les détails de la candidature.
     *
     * @param to L'adresse email du destinataire.
     * @param subject Le sujet de l'email.
     * @param content Le contenu de l'email.
     */
    private void sendEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("webmaster@gatsmapping.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    /**
     * Récupère une candidature par son ID.
     *
     * @param candidatureId L'ID de la candidature.
     * @return La candidature correspondante, ou null si elle n'existe pas.
     */
    public Candidature getCandidatureById(Integer candidatureId) {
        return candidatureRepository.findById(candidatureId)
                .orElse(null);
    }

    /**
     * Supprime une candidature par son ID.
     *
     * @param candidatureId L'ID de la candidature à supprimer.
     * @return true si la candidature a été supprimée, false si elle n'existe pas.
     */
    public boolean deleteCandidatureById(Integer candidatureId) {
        if (candidatureRepository.existsById(candidatureId)) {
            candidatureRepository.deleteById(candidatureId);
            return true;
        } else {
            return false; // Candidature non trouvée
        }
    }
}
