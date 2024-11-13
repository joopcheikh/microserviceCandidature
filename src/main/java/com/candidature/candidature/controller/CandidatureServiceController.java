package com.candidature.candidature.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.candidature.candidature.model.Candidature;
import com.candidature.candidature.model.User;
import com.candidature.candidature.repository.UserRepository;
import com.candidature.candidature.service.CandidatureServiceImpl;
import com.candidature.candidature.service.JwtService;

/**
 * @author cheikh diop
 *
 * Contrôleur pour gérer les candidatures. Il fournit des endpoints pour lister,
 * accepter, rejeter et mettre en attente des candidatures, ainsi que pour récupérer
 * les candidatures d'un utilisateur spécifique.
 */
@RestController
public class CandidatureServiceController {

    @Autowired
    private CandidatureServiceImpl candidatureServiceImpl;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Endpoint pour lister toutes les candidatures.
     *
     * @return ResponseEntity contenant la liste des candidatures.
     */
    @GetMapping("/get-candidatures")
    public ResponseEntity<List<Candidature>> listerCandidature() {
        return ResponseEntity.ok(candidatureServiceImpl.listerCandidature());
    }

    /**
     * Endpoint pour rejeter une candidature.
     *
     * @param candidatureId L'identifiant de la candidature à rejeter.
     * @return ResponseEntity contenant un message de succès.
     */
    @PostMapping("/reject-candidature")
    public ResponseEntity<String> rejeterCandidature(@RequestBody Integer candidatureId) {
        candidatureServiceImpl.rejeterCandidature(candidatureId);
        return ResponseEntity.ok("Candidature rejetée avec succès");
    }

    /**
     * Endpoint pour accepter une candidature.
     *
     * @param candidatureId L'identifiant de la candidature à accepter.
     * @return ResponseEntity contenant un message de succès.
     */
    @PostMapping("/accept-candidature")
    public ResponseEntity<String> acceptCandidature(@RequestBody Integer candidatureId) {
        candidatureServiceImpl.acceptCandidature(candidatureId);
        return ResponseEntity.ok("Candidature acceptée avec succès");
    }

    /**
     * Endpoint pour mettre une candidature en attente.
     *
     * @param candidatureId L'identifiant de la candidature à mettre en attente.
     * @return ResponseEntity contenant un message de succès.
     */
    @PostMapping("/await-candidature")
    public ResponseEntity<String> awaitCandidature(@RequestBody Integer candidatureId) {
        candidatureServiceImpl.awaitCandidature(candidatureId);
        return ResponseEntity.ok("Candidature mise en attente avec succès");
    }

    /**
     * Endpoint pour récupérer les candidatures d'un utilisateur authentifié.
     *
     * @param authHeader L'en-tête d'autorisation contenant le token JWT.
     * @return Liste des candidatures associées à l'utilisateur.
     * @throws RuntimeException si l'utilisateur n'est pas trouvé.
     */
    @GetMapping("/get-personnal/candidature")
    public List<Candidature> getConcours(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // Supprime "Bearer " du token
        String email = jwtService.extractUsername(token);
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new RuntimeException("Utilisateur non trouvé");
        }

        return candidatureServiceImpl.getConcoursByUserId(user.getId());
    }
}
