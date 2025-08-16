package com.thekade.nopolin.appointment_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRescheduleRequestDto {
    
    @NotNull(message = "Court hearing ID is required")
    private Long courtHearingId;
    
    private LocalDateTime requestedDate;
    
    @NotBlank(message = "Reason is required")
    private String reason;
}
