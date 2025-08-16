package com.thekade.nopolin.appointment_service.controller;

import com.thekade.nopolin.appointment_service.dto.CourtCaseDto;
import com.thekade.nopolin.appointment_service.dto.CourtHearingDto;
import com.thekade.nopolin.appointment_service.service.CourtCaseService;
import com.thekade.nopolin.appointment_service.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/court-cases")
@RequiredArgsConstructor
@Tag(name = "Court Case Management", description = "APIs for managing court cases and hearing dates")
public class CourtCaseController {

    private final CourtCaseService courtCaseService;
    private final JwtUtil jwtUtil;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('GOV_OFFICER')")
    @Operation(summary = "Create a new court case", description = "Create a new court case (Admin/Gov Officer only)")
    public ResponseEntity<CourtCaseDto> createCourtCase(@RequestBody CourtCaseDto courtCaseDto) {
        CourtCaseDto createdCase = courtCaseService.createCourtCase(courtCaseDto);
        return ResponseEntity.ok(createdCase);
    }

    @GetMapping("/case-number/{caseNumber}")
    @Operation(summary = "Get court case by case number", description = "Retrieve a court case by its case number")
    public ResponseEntity<CourtCaseDto> getCourtCaseByCaseNumber(@PathVariable String caseNumber) {
        return courtCaseService.getCourtCaseByCaseNumber(caseNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('CITIZEN')")
    @Operation(summary = "Get user court cases", description = "Retrieve all court cases for the authenticated user")
    public ResponseEntity<List<CourtCaseDto>> getUserCourtCases(Authentication authentication) {
        Long userId = jwtUtil.extractUserIdFromAuthentication(authentication);
        List<CourtCaseDto> courtCases = courtCaseService.getUserCourtCases(userId);
        return ResponseEntity.ok(courtCases);
    }

    @GetMapping("/user/upcoming-hearings")
    @PreAuthorize("hasRole('CITIZEN')")
    @Operation(summary = "Get user upcoming hearings", description = "Retrieve upcoming court hearings for the authenticated user")
    public ResponseEntity<List<CourtHearingDto>> getUserUpcomingHearings(Authentication authentication) {
        Long userId = jwtUtil.extractUserIdFromAuthentication(authentication);
        List<CourtHearingDto> hearings = courtCaseService.getUpcomingHearingsByUserId(userId);
        return ResponseEntity.ok(hearings);
    }

    @GetMapping("/{caseId}/hearings")
    @Operation(summary = "Get hearings by case ID", description = "Retrieve all hearings for a specific court case")
    public ResponseEntity<List<CourtHearingDto>> getHearingsByCaseId(@PathVariable Long caseId) {
        List<CourtHearingDto> hearings = courtCaseService.getHearingsByCaseId(caseId);
        return ResponseEntity.ok(hearings);
    }

    @PostMapping("/hearings")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GOV_OFFICER')")
    @Operation(summary = "Create a new court hearing", description = "Create a new court hearing (Admin/Gov Officer only)")
    public ResponseEntity<CourtHearingDto> createCourtHearing(@RequestBody CourtHearingDto hearingDto) {
        CourtHearingDto createdHearing = courtCaseService.createCourtHearing(hearingDto);
        return ResponseEntity.ok(createdHearing);
    }

    @GetMapping("/hearings/scheduled")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GOV_OFFICER')")
    @Operation(summary = "Get scheduled hearings in date range", description = "Retrieve scheduled hearings within a date range")
    public ResponseEntity<List<CourtHearingDto>> getScheduledHearingsInDateRange(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        List<CourtHearingDto> hearings = courtCaseService.getScheduledHearingsInDateRange(startDate, endDate);
        return ResponseEntity.ok(hearings);
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GOV_OFFICER')")
    @Operation(summary = "Deactivate court case", description = "Deactivate a court case (Admin/Gov Officer only)")
    public ResponseEntity<CourtCaseDto> deactivateCourtCase(@PathVariable Long id) {
        CourtCaseDto deactivatedCase = courtCaseService.deactivateCourtCase(id);
        return ResponseEntity.ok(deactivatedCase);
    }


}
