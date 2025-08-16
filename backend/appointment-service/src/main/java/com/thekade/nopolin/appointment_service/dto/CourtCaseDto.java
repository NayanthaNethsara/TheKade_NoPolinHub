package com.thekade.nopolin.appointment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourtCaseDto {
    private Long id;
    private String caseNumber;
    private Long userId;
    private String courtName;
    private String caseType;
    private String caseTitle;
    private Boolean isActive;
    private List<CourtHearingDto> hearings;
}
