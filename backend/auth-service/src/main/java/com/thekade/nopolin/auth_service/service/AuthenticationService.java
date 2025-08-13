package com.thekade.nopolin.auth_service.service;

import com.thekade.nopolin.auth_service.dto.AuthenticationRequest;
import com.thekade.nopolin.auth_service.dto.AuthenticationResponse;
import com.thekade.nopolin.auth_service.dto.RegisterRequest;
import com.thekade.nopolin.auth_service.entity.Role;
import com.thekade.nopolin.auth_service.entity.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.thekade.nopolin.auth_service.config.JwtService;
import com.thekade.nopolin.auth_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse register(RegisterRequest request) {
    var user = User.builder()
        .username(request.getUsername())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(request.getRole())
        .phoneNumber(request.getPhoneNumber())
        .build();
    
    repository.save(user);

    var accessToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user); // Generate refresh token

    return AuthenticationResponse.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }

  public AuthenticationResponse refreshToken(String refreshToken) {
    String username = jwtService.extractUsername(refreshToken); 
    var user = repository.findByUsername(username) // Retrieve user from DB
        .orElseThrow(() -> new RuntimeException("User not found"));

    if (!jwtService.validateToken(refreshToken, user)) {
        throw new RuntimeException("Invalid refresh token");
    }

    var newAccessToken = jwtService.generateToken(user);
    var newRefreshToken = jwtService.generateRefreshToken(user);

    return AuthenticationResponse.builder()
            .accessToken(newAccessToken)
            .refreshToken(newRefreshToken)
            .build();
}


  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getUsername(),
            request.getPassword()));
    var user = repository.findByUsername(request.getUsername())
        .orElseThrow();

    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user); // Generate refresh token


    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
        .refreshToken(refreshToken)
        .build();
  }

  public boolean userExists(Long id, String username, Role role) {
        return repository.existsByIdAndUsernameAndRole(id, username, role);
  }


  public boolean userExists(Long id, String username) {
    return repository.existsByIdAndUsername(id, username);
}

  public boolean checkAdminExists() {
    return repository.existsByUsernameAndRoleAndEmail("nayantha", Role.ADMIN , "nayanthanethsara@gmail.com");
  }

  public void updatePassword(String username, String password) {
    var user = repository.findByUsername(username);
    user.ifPresent(u -> {
      u.setPassword(passwordEncoder.encode(password));
      repository.save(u);
    });
  }


}
