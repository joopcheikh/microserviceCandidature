package com.registration.registration.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.registration.registration.model.Candidature;
import com.registration.registration.service.CandidatureServiceImpl;

@RestController
public class CandidatureServiceController {

    @Autowired
    private CandidatureServiceImpl candidatureServiceImpl;

    @GetMapping("/findcandidature")
    public ResponseEntity<List<Candidature>> listerCandidature() {
        return ResponseEntity.ok(candidatureServiceImpl.listerCandidature());
    }
    
}
