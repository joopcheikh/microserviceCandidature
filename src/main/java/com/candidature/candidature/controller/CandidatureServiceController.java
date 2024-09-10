package com.candidature.candidature.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    
}
