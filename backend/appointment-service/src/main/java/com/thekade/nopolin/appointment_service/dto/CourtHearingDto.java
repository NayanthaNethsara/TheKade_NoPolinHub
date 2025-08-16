package com.thekade.nopolin.appointment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourtHearingDto {
    private Long id;
    private Long courtCaseId;
    private LocalDateTime hearingDate;
    private String hearingType;
    private String courtRoom;
    private String judgeName;
    private String status;
    private String notes;
}
