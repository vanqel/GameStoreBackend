package com.gamestore.backend.v1.apps.authorization.repository;

import com.gamestore.backend.v1.apps.authorization.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<Users, UUID> {
    Optional<Users> findByUsername(String username);
    Users findByUid(UUID uid);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
