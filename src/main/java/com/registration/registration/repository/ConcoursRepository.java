package com.registration.registration.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.registration.registration.model.Concours;

public interface ConcoursRepository extends JpaRepository<Concours, Integer>{
    
}
