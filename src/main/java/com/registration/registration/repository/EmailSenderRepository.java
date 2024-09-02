package com.registration.registration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.registration.registration.model.EmailSender;

import jakarta.transaction.Transactional;

public interface  EmailSenderRepository extends JpaRepository<EmailSender, Long>{

    boolean existsByEmail(String email); 
    @Modifying
    @Transactional 
    @Query("UPDATE User u SET u.password = ?2 WHERE u.email = ?1")
    void updatePassword(String email, String password);
    
}