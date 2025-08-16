package com.thekade.nopolin.appointment_service.controller;

import com.thekade.nopolin.appointment_service.dto.CreateRescheduleRequestDto;
import com.thekade.nopolin.appointment_service.dto.RescheduleRequestDto;
import com.thekade.nopolin.appointment_service.entity.RescheduleStatus;
import com.thekade.nopolin.appointment_service.service.RescheduleRequestService;
import com.thekade.nopolin.appointment_service.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reschedule-requests")
@RequiredArgsConstructor
@Tag(name = "Reschedule Request Management", description = "APIs for managing court hearing reschedule requests")
public class RescheduleRequestController {

    private final RescheduleRequestService rescheduleRequestService;
    private final JwtUtil jwtUtil;

    @PostMapping
    @PreAuthorize("hasRole('CITIZEN')")
    @Operation(summary = "Create reschedule request", description = "Submit a request to reschedule a court hearing")
    public ResponseEntity<RescheduleRequestDto> createRescheduleRequest(
            @Valid @RequestBody CreateRescheduleRequestDto requestDto,
            Authentication authentication) {
        Long userId = jwtUtil.extractUserIdFromAuthentication(authentication);
        RescheduleRequestDto request = rescheduleRequestService.createRescheduleRequest(userId, requestDto);
        return ResponseEntity.ok(request);
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('CITIZEN')")
    @Operation(summary = "Get user reschedule requests", description = "Retrieve all reschedule requests for the authenticated user")
    public ResponseEntity<List<RescheduleRequestDto>> getUserRescheduleRequests(Authentication authentication) {
        Long userId = jwtUtil.extractUserIdFromAuthentication(authentication);
        List<RescheduleRequestDto> requests = rescheduleRequestService.getUserRescheduleRequests(userId);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/user/status/{status}")
    @PreAuthorize("hasRole('CITIZEN')")
    @Operation(summary = "Get user reschedule requests by status", description = "Retrieve user reschedule requests filtered by status")
    public ResponseEntity<List<RescheduleRequestDto>> getUserRescheduleRequestsByStatus(
            @PathVariable RescheduleStatus status,
            Authentication authentication) {
        Long userId = jwtUtil.extractUserIdFromAuthentication(authentication);
        List<RescheduleRequestDto> requests = rescheduleRequestService.getUserRescheduleRequestsByStatus(userId, status);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GOV_OFFICER')")
    @Operation(summary = "Get pending reschedule requests", description = "Retrieve all pending reschedule requests (Admin/Gov Officer only)")
    public ResponseEntity<List<RescheduleRequestDto>> getPendingRescheduleRequests() {
        List<RescheduleRequestDto> requests = rescheduleRequestService.getPendingRescheduleRequests();
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get reschedule request by ID", description = "Retrieve a specific reschedule request by ID")
    public ResponseEntity<RescheduleRequestDto> getRescheduleRequestById(@PathVariable Long id) {
        return rescheduleRequestService.getRescheduleRequestById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GOV_OFFICER')")
    @Operation(summary = "Approve reschedule request", description = "Approve a reschedule request (Admin/Gov Officer only)")
    public ResponseEntity<RescheduleRequestDto> approveRescheduleRequest(
            @PathVariable Long id,
            @RequestParam(required = false) String adminNotes) {
        RescheduleRequestDto request = rescheduleRequestService.approveRescheduleRequest(id, adminNotes);
        return ResponseEntity.ok(request);
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GOV_OFFICER')")
    @Operation(summary = "Reject reschedule request", description = "Reject a reschedule request (Admin/Gov Officer only)")
    public ResponseEntity<RescheduleRequestDto> rejectRescheduleRequest(
            @PathVariable Long id,
            @RequestParam(required = false) String adminNotes) {
        RescheduleRequestDto request = rescheduleRequestService.rejectRescheduleRequest(id, adminNotes);
        return ResponseEntity.ok(request);
    }


}
