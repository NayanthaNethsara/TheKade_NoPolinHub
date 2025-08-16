package com.thekade.nopolin.appointment_service.service;

import com.thekade.nopolin.appointment_service.dto.AppointmentDto;
import com.thekade.nopolin.appointment_service.dto.CreateAppointmentRequest;
import com.thekade.nopolin.appointment_service.entity.Appointment;
import com.thekade.nopolin.appointment_service.entity.AppointmentStatus;
import com.thekade.nopolin.appointment_service.entity.Lawyer;
import com.thekade.nopolin.appointment_service.repository.AppointmentRepository;
import com.thekade.nopolin.appointment_service.repository.LawyerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final LawyerRepository lawyerRepository;

    public AppointmentDto createAppointment(Long userId, CreateAppointmentRequest request) {
        // Check if lawyer exists
        Lawyer lawyer = lawyerRepository.findById(request.getLawyerId())
                .orElseThrow(() -> new RuntimeException("Lawyer not found"));

        // Check if lawyer is available
        if (!lawyer.getIsActive() || !lawyer.getIsVerified()) {
            throw new RuntimeException("Lawyer is not available for appointments");
        }

        // Check for conflicting appointments
        LocalDateTime endTime = request.getAppointmentDate().plusMinutes(request.getDurationMinutes());
        List<Appointment> conflictingAppointments = appointmentRepository.findConflictingAppointments(
                request.getLawyerId(), request.getAppointmentDate(), endTime);

        if (!conflictingAppointments.isEmpty()) {
            throw new RuntimeException("Lawyer is not available at the requested time");
        }

        Appointment appointment = Appointment.builder()
                .userId(userId)
                .lawyer(lawyer)
                .appointmentDate(request.getAppointmentDate())
                .durationMinutes(request.getDurationMinutes())
                .appointmentType(request.getAppointmentType())
                .status(AppointmentStatus.PENDING)
                .legalIssueType(request.getLegalIssueType())
                .description(request.getDescription())
                .isFreeConsultation(request.getIsFreeConsultation())
                .build();

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return mapToDto(savedAppointment);
    }

    public List<AppointmentDto> getUserAppointments(Long userId) {
        return appointmentRepository.findByUserId(userId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<AppointmentDto> getLawyerAppointments(Long lawyerId) {
        return appointmentRepository.findByLawyerId(lawyerId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<AppointmentDto> getUserAppointmentsByStatus(Long userId, AppointmentStatus status) {
        return appointmentRepository.findByUserIdAndStatus(userId, status)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<AppointmentDto> getLawyerAppointmentsByStatus(Long lawyerId, AppointmentStatus status) {
        return appointmentRepository.findByLawyerIdAndStatus(lawyerId, status)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public Optional<AppointmentDto> getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .map(this::mapToDto);
    }

    public AppointmentDto confirmAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setStatus(AppointmentStatus.CONFIRMED);
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return mapToDto(savedAppointment);
    }

    public AppointmentDto cancelAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setStatus(AppointmentStatus.CANCELLED);
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return mapToDto(savedAppointment);
    }

    public AppointmentDto completeAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setStatus(AppointmentStatus.COMPLETED);
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return mapToDto(savedAppointment);
    }

    public List<AppointmentDto> getUpcomingAppointments(LocalDateTime startDate, LocalDateTime endDate) {
        return appointmentRepository.findUpcomingAppointments(startDate, endDate)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private AppointmentDto mapToDto(Appointment appointment) {
        return AppointmentDto.builder()
                .id(appointment.getId())
                .userId(appointment.getUserId())
                .lawyerId(appointment.getLawyer().getId())
                .lawyerName(appointment.getLawyer().getName())
                .appointmentDate(appointment.getAppointmentDate())
                .durationMinutes(appointment.getDurationMinutes())
                .appointmentType(appointment.getAppointmentType())
                .status(appointment.getStatus())
                .legalIssueType(appointment.getLegalIssueType())
                .description(appointment.getDescription())
                .isFreeConsultation(appointment.getIsFreeConsultation())
                .createdAt(appointment.getCreatedAt())
                .updatedAt(appointment.getUpdatedAt())
                .build();
    }
}
