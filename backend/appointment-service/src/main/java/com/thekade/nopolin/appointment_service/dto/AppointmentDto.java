package com.thekade.nopolin.appointment_service.dto;

import com.thekade.nopolin.appointment_service.entity.AppointmentStatus;
import com.thekade.nopolin.appointment_service.entity.AppointmentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDto {
    private Long id;
    private Long userId;
    private Long lawyerId;
    private String lawyerName;
    private LocalDateTime appointmentDate;
    private Integer durationMinutes;
    private AppointmentType appointmentType;
    private AppointmentStatus status;
    private String legalIssueType;
    private String description;
    private Boolean isFreeConsultation;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
