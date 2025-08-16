package com.thekade.nopolin.appointment_service.dto;

import com.thekade.nopolin.appointment_service.entity.AppointmentType;
import jakarta.validation.constraints.Future;
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
public class CreateAppointmentRequest {
    
    @NotNull(message = "Lawyer ID is required")
    private Long lawyerId;
    
    @NotNull(message = "Appointment date is required")
    @Future(message = "Appointment date must be in the future")
    private LocalDateTime appointmentDate;
    
    private Integer durationMinutes = 60;
    
    @NotNull(message = "Appointment type is required")
    private AppointmentType appointmentType;
    
    private String legalIssueType;
    private String description;
    private Boolean isFreeConsultation = false;
}
