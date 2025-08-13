package com.thekade.nopolin.auth_service.service;

import com.thekade.nopolin.auth_service.dto.UserResponse;
import com.thekade.nopolin.auth_service.entity.Role;
import com.thekade.nopolin.auth_service.entity.User;
import com.thekade.nopolin.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

public interface UserService {
    List<UserResponse> getAllUsers();
    List<UserResponse> getUsersByRole(Role role);
    void deleteUserById(Long id);
    void deactivateUser(Long id);
    void activateUser(Long id);
}

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private UserResponse mapToDto(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .isActive(user.isActive())
                .build();
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getRole() != Role.ADMIN)
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponse> getUsersByRole(Role role) {
        return userRepository.findByRole(role)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public void deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public void activateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        user.setActive(true);
        userRepository.save(user);
    }
}