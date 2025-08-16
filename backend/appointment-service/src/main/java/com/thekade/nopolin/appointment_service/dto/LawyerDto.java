package com.thekade.nopolin.appointment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LawyerDto {
    private Long id;
    private Long userId;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String city;
    private String specialization;
    private Integer experienceYears;
    private Double hourlyRate;
    private Boolean isFreeConsultationAvailable;
    private Boolean isVerified;
    private Boolean isActive;
    private String bio;
}
