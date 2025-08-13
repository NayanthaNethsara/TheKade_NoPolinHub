package com.thekade.nopolin.auth_service.dto;

import com.thekade.nopolin.auth_service.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  private String username;
  private String email;
  private String password;
  private String phoneNumber;
  private Role role;
}