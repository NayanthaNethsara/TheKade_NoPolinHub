package com.thekade.nopolin.authservice.repository;

import com.thekade.nopolin.authservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByNic(String nic);

    boolean existsByPhone(String phone);
}
