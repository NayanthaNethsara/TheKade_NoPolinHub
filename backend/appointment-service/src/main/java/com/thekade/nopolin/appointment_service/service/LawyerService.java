package com.thekade.nopolin.appointment_service.service;

import com.thekade.nopolin.appointment_service.dto.CreateLawyerRequest;
import com.thekade.nopolin.appointment_service.dto.LawyerDto;
import com.thekade.nopolin.appointment_service.entity.Lawyer;
import com.thekade.nopolin.appointment_service.repository.LawyerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LawyerService {

    private final LawyerRepository lawyerRepository;

    public LawyerDto createLawyer(CreateLawyerRequest request) {
        Lawyer lawyer = Lawyer.builder()
                .userId(request.getUserId())
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .city(request.getCity())
                .specialization(request.getSpecialization())
                .experienceYears(request.getExperienceYears())
                .hourlyRate(request.getHourlyRate())
                .isFreeConsultationAvailable(request.getIsFreeConsultationAvailable())
                .bio(request.getBio())
                .isVerified(false)
                .isActive(true)
                .build();

        Lawyer savedLawyer = lawyerRepository.save(lawyer);
        return mapToDto(savedLawyer);
    }

    public List<LawyerDto> getAllVerifiedLawyers() {
        return lawyerRepository.findByIsVerifiedTrueAndIsActiveTrue()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<LawyerDto> getLawyersBySpecialization(String specialization) {
        return lawyerRepository.findBySpecializationAndIsVerifiedTrueAndIsActiveTrue(specialization)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<LawyerDto> getLawyersByCity(String city) {
        return lawyerRepository.findByCityAndIsVerifiedTrueAndIsActiveTrue(city)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<LawyerDto> getAvailableLawyers(String specialization, String city) {
        return lawyerRepository.findAvailableLawyers(specialization, city)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<LawyerDto> getFreeConsultationLawyers() {
        return lawyerRepository.findByIsFreeConsultationAvailableTrueAndIsVerifiedTrueAndIsActiveTrue()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public Optional<LawyerDto> getLawyerById(Long id) {
        return lawyerRepository.findById(id)
                .map(this::mapToDto);
    }

    public Optional<LawyerDto> getLawyerByUserId(Long userId) {
        return lawyerRepository.findByUserId(userId)
                .map(this::mapToDto);
    }

    public LawyerDto verifyLawyer(Long id) {
        Lawyer lawyer = lawyerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lawyer not found"));
        
        lawyer.setIsVerified(true);
        Lawyer savedLawyer = lawyerRepository.save(lawyer);
        return mapToDto(savedLawyer);
    }

    public LawyerDto deactivateLawyer(Long id) {
        Lawyer lawyer = lawyerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lawyer not found"));
        
        lawyer.setIsActive(false);
        Lawyer savedLawyer = lawyerRepository.save(lawyer);
        return mapToDto(savedLawyer);
    }

    private LawyerDto mapToDto(Lawyer lawyer) {
        return LawyerDto.builder()
                .id(lawyer.getId())
                .userId(lawyer.getUserId())
                .name(lawyer.getName())
                .email(lawyer.getEmail())
                .phoneNumber(lawyer.getPhoneNumber())
                .address(lawyer.getAddress())
                .city(lawyer.getCity())
                .specialization(lawyer.getSpecialization())
                .experienceYears(lawyer.getExperienceYears())
                .hourlyRate(lawyer.getHourlyRate())
                .isFreeConsultationAvailable(lawyer.getIsFreeConsultationAvailable())
                .isVerified(lawyer.getIsVerified())
                .isActive(lawyer.getIsActive())
                .bio(lawyer.getBio())
                .build();
    }
}
