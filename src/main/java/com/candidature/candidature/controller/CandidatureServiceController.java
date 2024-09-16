package com.candidature.candidature.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.candidature.candidature.model.Candidature;
import com.candidature.candidature.service.CandidatureServiceImpl;



@RestController
public class CandidatureServiceController {

    @Autowired
    private CandidatureServiceImpl candidatureServiceImpl;

   
    @GetMapping("/get-candidatures")
    public ResponseEntity<List<Candidature>> listerCandidature() {
        return ResponseEntity.ok(candidatureServiceImpl.listerCandidature());
    }
    @PostMapping("/reject-candidature")
    public ResponseEntity<String> rejeterCandidature(@RequestBody Integer candidatureId) {
        candidatureServiceImpl.rejeterCandidature(candidatureId);
        return ResponseEntity.ok("Candidature rejetée avec succès");
    }

    @PostMapping("/accept-candidature")
    public ResponseEntity<String> acceptCandidature(@RequestBody Integer candidatureId) {
        candidatureServiceImpl.acceptCandidature(candidatureId);
        return ResponseEntity.ok("Candidature acceptée avec succès");
    }

    @PostMapping("/await-candidature")
    public ResponseEntity<String> awaitCandidature(@RequestBody Integer candidatureId) {
        candidatureServiceImpl.awaitCandidature(candidatureId);
        return ResponseEntity.ok("Candidature acceptée avec succès");
    }
    
}
