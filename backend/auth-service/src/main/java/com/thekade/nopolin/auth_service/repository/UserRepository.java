package com.thekade.nopolin.auth_service.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thekade.nopolin.auth_service.entity.User;
import com.thekade.nopolin.auth_service.entity.Role;


public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.role = :role")
    List<User> findAllByRole(@Param("role") Role role);

    List<User> findByRole(Role role);

    Optional<User> findByIdAndRole(Long id, Role role);

    boolean existsByIdAndUsernameAndRole(Long id, String username, Role role);

    boolean existsByIdAndUsername(Long id, String username);

    boolean existsByUsernameAndRoleAndEmail(String username, Role role , String email);

}