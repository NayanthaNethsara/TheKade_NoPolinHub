package com.thekade.nopolin.authservice.controller;

import com.thekade.nopolin.authservice.dto.RegisterRequest;
import com.thekade.nopolin.authservice.entity.User;
import com.thekade.nopolin.authservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest request) {

        if (userService.usernameExists(request.getUsername())) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        if (userService.emailExists(request.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        if (userService.nicExists(request.getNic())) {
            return ResponseEntity.badRequest().body("Error: NIC is already registered!");
        }

        if (userService.phoneExists(request.getPhone())) {
            return ResponseEntity.badRequest().body("Error: Phone number is already registered!");
        }

        User user = userService.registerUser(request);
        return ResponseEntity.ok("User registered successfully with username: " + user.getUsername());
    }
}
