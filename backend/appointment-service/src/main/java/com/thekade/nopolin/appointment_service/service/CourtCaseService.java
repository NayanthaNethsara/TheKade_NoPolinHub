package com.thekade.nopolin.appointment_service.service;

import com.thekade.nopolin.appointment_service.dto.CourtCaseDto;
import com.thekade.nopolin.appointment_service.dto.CourtHearingDto;
import com.thekade.nopolin.appointment_service.entity.CourtCase;
import com.thekade.nopolin.appointment_service.entity.CourtHearing;
import com.thekade.nopolin.appointment_service.repository.CourtCaseRepository;
import com.thekade.nopolin.appointment_service.repository.CourtHearingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourtCaseService {

    private final CourtCaseRepository courtCaseRepository;
    private final CourtHearingRepository courtHearingRepository;

    public CourtCaseDto createCourtCase(CourtCaseDto courtCaseDto) {
        CourtCase courtCase = CourtCase.builder()
                .caseNumber(courtCaseDto.getCaseNumber())
                .userId(courtCaseDto.getUserId())
                .courtName(courtCaseDto.getCourtName())
                .caseType(courtCaseDto.getCaseType())
                .caseTitle(courtCaseDto.getCaseTitle())
                .isActive(true)
                .build();

        CourtCase savedCourtCase = courtCaseRepository.save(courtCase);
        return mapToDto(savedCourtCase);
    }

    public Optional<CourtCaseDto> getCourtCaseByCaseNumber(String caseNumber) {
        return courtCaseRepository.findByCaseNumber(caseNumber)
                .map(this::mapToDto);
    }

    public Optional<CourtCaseDto> getCourtCaseByCaseNumberAndUserId(String caseNumber, Long userId) {
        return courtCaseRepository.findByCaseNumberAndUserId(caseNumber, userId)
                .map(this::mapToDto);
    }

    public List<CourtCaseDto> getUserCourtCases(Long userId) {
        return courtCaseRepository.findByUserIdAndIsActiveTrue(userId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CourtHearingDto> getUpcomingHearingsByUserId(Long userId) {
        LocalDateTime startDate = LocalDateTime.now();
        return courtHearingRepository.findUpcomingHearingsByUserId(userId, startDate)
                .stream()
                .map(this::mapHearingToDto)
                .collect(Collectors.toList());
    }

    public List<CourtHearingDto> getHearingsByCaseId(Long caseId) {
        return courtHearingRepository.findByCourtCaseId(caseId)
                .stream()
                .map(this::mapHearingToDto)
                .collect(Collectors.toList());
    }

    public CourtHearingDto createCourtHearing(CourtHearingDto hearingDto) {
        CourtCase courtCase = courtCaseRepository.findById(hearingDto.getCourtCaseId())
                .orElseThrow(() -> new RuntimeException("Court case not found"));

        CourtHearing hearing = CourtHearing.builder()
                .courtCase(courtCase)
                .hearingDate(hearingDto.getHearingDate())
                .hearingType(hearingDto.getHearingType())
                .courtRoom(hearingDto.getCourtRoom())
                .judgeName(hearingDto.getJudgeName())
                .status(hearingDto.getStatus())
                .notes(hearingDto.getNotes())
                .build();

        CourtHearing savedHearing = courtHearingRepository.save(hearing);
        return mapHearingToDto(savedHearing);
    }

    public List<CourtHearingDto> getScheduledHearingsInDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return courtHearingRepository.findScheduledHearingsInDateRange(startDate, endDate)
                .stream()
                .map(this::mapHearingToDto)
                .collect(Collectors.toList());
    }

    public CourtCaseDto deactivateCourtCase(Long id) {
        CourtCase courtCase = courtCaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Court case not found"));

        courtCase.setIsActive(false);
        CourtCase savedCourtCase = courtCaseRepository.save(courtCase);
        return mapToDto(savedCourtCase);
    }

    private CourtCaseDto mapToDto(CourtCase courtCase) {
        List<CourtHearingDto> hearings = courtCase.getHearings() != null ?
                courtCase.getHearings().stream()
                        .map(this::mapHearingToDto)
                        .collect(Collectors.toList()) : null;

        return CourtCaseDto.builder()
                .id(courtCase.getId())
                .caseNumber(courtCase.getCaseNumber())
                .userId(courtCase.getUserId())
                .courtName(courtCase.getCourtName())
                .caseType(courtCase.getCaseType())
                .caseTitle(courtCase.getCaseTitle())
                .isActive(courtCase.getIsActive())
                .hearings(hearings)
                .build();
    }

    private CourtHearingDto mapHearingToDto(CourtHearing hearing) {
        return CourtHearingDto.builder()
                .id(hearing.getId())
                .courtCaseId(hearing.getCourtCase().getId())
                .hearingDate(hearing.getHearingDate())
                .hearingType(hearing.getHearingType())
                .courtRoom(hearing.getCourtRoom())
                .judgeName(hearing.getJudgeName())
                .status(hearing.getStatus())
                .notes(hearing.getNotes())
                .build();
    }
}
