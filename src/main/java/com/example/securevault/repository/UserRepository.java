package com.example.securevault.repository;

import com.example.securevault.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findBySub(String sub);
    Optional<User> findByEmail(String email);
}

