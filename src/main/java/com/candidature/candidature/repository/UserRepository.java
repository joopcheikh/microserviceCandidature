package com.candidature.candidature.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.candidature.candidature.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findUserByEmail(String email);
}
