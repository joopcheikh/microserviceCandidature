package com.candidature.candidature.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candidature.candidature.model.Candidature;
import com.candidature.candidature.repository.CandidatureRepository;

@Service
public class CandidatureServiceImpl implements CandidatureServices{

    @Autowired
    private CandidatureRepository candidatureRepository;

    @Override
    public List<Candidature> listerCandidature() {
        return candidatureRepository.findAll();
    }
    
}
