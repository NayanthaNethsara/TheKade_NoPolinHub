package com.thekade.nopolin.appointment_service.dto;

import com.thekade.nopolin.appointment_service.entity.RescheduleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RescheduleRequestDto {
    private Long id;
    private Long userId;
    private Long courtHearingId;
    private LocalDateTime requestedDate;
    private String reason;
    private RescheduleStatus status;
    private String adminNotes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
