package com.candidature.candidature.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.candidature.candidature.model.Candidature;

public interface CandidatureRepository extends JpaRepository<Candidature, Integer>{

    Optional<Candidature> findCandidatureByUsername(String username);
    
}
