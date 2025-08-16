package com.thekade.nopolin.appointment_service.controller;

import com.thekade.nopolin.appointment_service.dto.AppointmentDto;
import com.thekade.nopolin.appointment_service.dto.CreateAppointmentRequest;
import com.thekade.nopolin.appointment_service.entity.AppointmentStatus;
import com.thekade.nopolin.appointment_service.service.AppointmentService;
import com.thekade.nopolin.appointment_service.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Tag(name = "Appointment Management", description = "APIs for managing legal consultation appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final JwtUtil jwtUtil;

    @PostMapping
    @PreAuthorize("hasRole('CITIZEN')")
    @Operation(summary = "Book an appointment", description = "Book a new legal consultation appointment")
    public ResponseEntity<AppointmentDto> bookAppointment(
            @Valid @RequestBody CreateAppointmentRequest request,
            Authentication authentication) {
        Long userId = jwtUtil.extractUserIdFromAuthentication(authentication);
        AppointmentDto appointment = appointmentService.createAppointment(userId, request);
        return ResponseEntity.ok(appointment);
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('CITIZEN')")
    @Operation(summary = "Get user appointments", description = "Retrieve all appointments for the authenticated user")
    public ResponseEntity<List<AppointmentDto>> getUserAppointments(Authentication authentication) {
        Long userId = jwtUtil.extractUserIdFromAuthentication(authentication);
        List<AppointmentDto> appointments = appointmentService.getUserAppointments(userId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/user/status/{status}")
    @PreAuthorize("hasRole('CITIZEN')")
    @Operation(summary = "Get user appointments by status", description = "Retrieve user appointments filtered by status")
    public ResponseEntity<List<AppointmentDto>> getUserAppointmentsByStatus(
            @PathVariable AppointmentStatus status,
            Authentication authentication) {
        Long userId = jwtUtil.extractUserIdFromAuthentication(authentication);
        List<AppointmentDto> appointments = appointmentService.getUserAppointmentsByStatus(userId, status);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/lawyer/{lawyerId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GOV_OFFICER')")
    @Operation(summary = "Get lawyer appointments", description = "Retrieve all appointments for a specific lawyer")
    public ResponseEntity<List<AppointmentDto>> getLawyerAppointments(@PathVariable Long lawyerId) {
        List<AppointmentDto> appointments = appointmentService.getLawyerAppointments(lawyerId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/lawyer/{lawyerId}/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GOV_OFFICER')")
    @Operation(summary = "Get lawyer appointments by status", description = "Retrieve lawyer appointments filtered by status")
    public ResponseEntity<List<AppointmentDto>> getLawyerAppointmentsByStatus(
            @PathVariable Long lawyerId,
            @PathVariable AppointmentStatus status) {
        List<AppointmentDto> appointments = appointmentService.getLawyerAppointmentsByStatus(lawyerId, status);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get appointment by ID", description = "Retrieve a specific appointment by ID")
    public ResponseEntity<AppointmentDto> getAppointmentById(@PathVariable Long id) {
        return appointmentService.getAppointmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/confirm")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GOV_OFFICER')")
    @Operation(summary = "Confirm appointment", description = "Confirm a pending appointment")
    public ResponseEntity<AppointmentDto> confirmAppointment(@PathVariable Long id) {
        AppointmentDto appointment = appointmentService.confirmAppointment(id);
        return ResponseEntity.ok(appointment);
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('CITIZEN') or hasRole('ADMIN') or hasRole('GOV_OFFICER')")
    @Operation(summary = "Cancel appointment", description = "Cancel an appointment")
    public ResponseEntity<AppointmentDto> cancelAppointment(@PathVariable Long id) {
        AppointmentDto appointment = appointmentService.cancelAppointment(id);
        return ResponseEntity.ok(appointment);
    }

    @PutMapping("/{id}/complete")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GOV_OFFICER')")
    @Operation(summary = "Complete appointment", description = "Mark an appointment as completed")
    public ResponseEntity<AppointmentDto> completeAppointment(@PathVariable Long id) {
        AppointmentDto appointment = appointmentService.completeAppointment(id);
        return ResponseEntity.ok(appointment);
    }

    @GetMapping("/upcoming")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GOV_OFFICER')")
    @Operation(summary = "Get upcoming appointments", description = "Retrieve upcoming appointments within a date range")
    public ResponseEntity<List<AppointmentDto>> getUpcomingAppointments(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        List<AppointmentDto> appointments = appointmentService.getUpcomingAppointments(startDate, endDate);
        return ResponseEntity.ok(appointments);
    }


}
