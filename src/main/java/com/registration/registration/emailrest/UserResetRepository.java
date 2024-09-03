package com.registration.registration.emailrest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;

public interface UserResetRepository extends JpaRepository<UserReset, Long> {
    boolean existsByEmail(String email); 
    @Modifying
    @Transactional 
    @Query("UPDATE UserReset u SET u.password = ?2 WHERE u.email = ?1")
    void updatePassword(String email, String password);
}
