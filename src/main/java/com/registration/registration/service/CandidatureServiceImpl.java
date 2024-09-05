package com.registration.registration.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.registration.registration.model.Candidature;
import com.registration.registration.repository.CandidatureRepository;

@Service
public class CandidatureServiceImpl implements CandidatureServices{

    @Autowired
    private CandidatureRepository candidatureRepository;

    @Override
    public List<Candidature> listerCandidature() {
        return candidatureRepository.findAll();
    }
    
}
