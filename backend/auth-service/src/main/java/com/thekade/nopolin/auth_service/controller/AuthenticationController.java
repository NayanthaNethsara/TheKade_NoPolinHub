package com.thekade.nopolin.auth_service.controller;


import com.thekade.nopolin.auth_service.dto.AuthenticationRequest;
import com.thekade.nopolin.auth_service.dto.AuthenticationResponse;
import com.thekade.nopolin.auth_service.dto.RefreshTokenRequest;
import com.thekade.nopolin.auth_service.dto.RegisterRequest;
import com.thekade.nopolin.auth_service.dto.UpdatePasswordRequest;
import com.thekade.nopolin.auth_service.dto.UserExistsRequest;
import com.thekade.nopolin.auth_service.repository.UserRepository;
import com.thekade.nopolin.auth_service.service.AuthenticationService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private static final Logger LOGGER = Logger.getLogger(AuthenticationController.class.getName());

    private final UserRepository userRepository;

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(service.refreshToken(request.getRefreshToken()));
    }

    @PostMapping("/exists")
    public ResponseEntity<Boolean> checkUserExists(
            @RequestBody UserExistsRequest request) {
        boolean exists = service.userExists(request.getId(), request.getUsername());
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/exists/admin")
    public ResponseEntity<Boolean> checkAdminExists() {
        boolean exists = service.checkAdminExists();
        return ResponseEntity.ok(exists);
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<Void> updatePassword(
            @RequestBody UpdatePasswordRequest request) {
        service.updatePassword(request.getUsername(), request.getPassword());
        return ResponseEntity.ok().build();
    }

}
