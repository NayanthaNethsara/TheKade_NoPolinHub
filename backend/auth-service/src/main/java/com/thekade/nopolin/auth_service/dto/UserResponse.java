package com.thekade.nopolin.auth_service.dto;

import com.thekade.nopolin.auth_service.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String phoneNumber;
    private Role role;
    private boolean isActive;
}
