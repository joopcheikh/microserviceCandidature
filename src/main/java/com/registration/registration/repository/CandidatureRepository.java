package com.registration.registration.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.registration.registration.model.Candidature;

public interface CandidatureRepository extends JpaRepository<Candidature, Integer>{

    Optional<Candidature> findCandidatureByUsername(String username);
    
}
