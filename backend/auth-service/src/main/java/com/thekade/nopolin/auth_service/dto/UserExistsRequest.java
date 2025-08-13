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
public class UserExistsRequest {

        private Long id;
        private String username;
        private Role role;

}
