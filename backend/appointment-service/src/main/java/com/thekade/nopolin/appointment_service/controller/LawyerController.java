package com.thekade.nopolin.appointment_service.controller;

import com.thekade.nopolin.appointment_service.dto.CreateLawyerRequest;
import com.thekade.nopolin.appointment_service.dto.LawyerDto;
import com.thekade.nopolin.appointment_service.service.LawyerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lawyers")
@RequiredArgsConstructor
@Tag(name = "Lawyer Management", description = "APIs for managing lawyers and legal aid providers")
public class LawyerController {

    private final LawyerService lawyerService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('GOV_OFFICER')")
    @Operation(summary = "Create a new lawyer profile", description = "Create a new lawyer profile (Admin/Gov Officer only)")
    public ResponseEntity<LawyerDto> createLawyer(@Valid @RequestBody CreateLawyerRequest request) {
        LawyerDto createdLawyer = lawyerService.createLawyer(request);
        return ResponseEntity.ok(createdLawyer);
    }

    @GetMapping
    @Operation(summary = "Get all verified lawyers", description = "Retrieve all verified and active lawyers")
    public ResponseEntity<List<LawyerDto>> getAllVerifiedLawyers() {
        List<LawyerDto> lawyers = lawyerService.getAllVerifiedLawyers();
        return ResponseEntity.ok(lawyers);
    }

    @GetMapping("/specialization/{specialization}")
    @Operation(summary = "Get lawyers by specialization", description = "Retrieve lawyers filtered by legal specialization")
    public ResponseEntity<List<LawyerDto>> getLawyersBySpecialization(@PathVariable String specialization) {
        List<LawyerDto> lawyers = lawyerService.getLawyersBySpecialization(specialization);
        return ResponseEntity.ok(lawyers);
    }

    @GetMapping("/city/{city}")
    @Operation(summary = "Get lawyers by city", description = "Retrieve lawyers filtered by city")
    public ResponseEntity<List<LawyerDto>> getLawyersByCity(@PathVariable String city) {
        List<LawyerDto> lawyers = lawyerService.getLawyersByCity(city);
        return ResponseEntity.ok(lawyers);
    }

    @GetMapping("/search")
    @Operation(summary = "Search available lawyers", description = "Search for available lawyers by specialization and city")
    public ResponseEntity<List<LawyerDto>> searchAvailableLawyers(
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) String city) {
        List<LawyerDto> lawyers = lawyerService.getAvailableLawyers(specialization, city);
        return ResponseEntity.ok(lawyers);
    }

    @GetMapping("/free-consultation")
    @Operation(summary = "Get lawyers offering free consultation", description = "Retrieve lawyers who offer free consultations")
    public ResponseEntity<List<LawyerDto>> getFreeConsultationLawyers() {
        List<LawyerDto> lawyers = lawyerService.getFreeConsultationLawyers();
        return ResponseEntity.ok(lawyers);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get lawyer by ID", description = "Retrieve a specific lawyer by their ID")
    public ResponseEntity<LawyerDto> getLawyerById(@PathVariable Long id) {
        return lawyerService.getLawyerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get lawyer by user ID", description = "Retrieve a lawyer profile by their user ID")
    public ResponseEntity<LawyerDto> getLawyerByUserId(@PathVariable Long userId) {
        return lawyerService.getLawyerByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/verify")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GOV_OFFICER')")
    @Operation(summary = "Verify a lawyer", description = "Verify a lawyer profile (Admin/Gov Officer only)")
    public ResponseEntity<LawyerDto> verifyLawyer(@PathVariable Long id) {
        LawyerDto verifiedLawyer = lawyerService.verifyLawyer(id);
        return ResponseEntity.ok(verifiedLawyer);
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GOV_OFFICER')")
    @Operation(summary = "Deactivate a lawyer", description = "Deactivate a lawyer profile (Admin/Gov Officer only)")
    public ResponseEntity<LawyerDto> deactivateLawyer(@PathVariable Long id) {
        LawyerDto deactivatedLawyer = lawyerService.deactivateLawyer(id);
        return ResponseEntity.ok(deactivatedLawyer);
    }
}
