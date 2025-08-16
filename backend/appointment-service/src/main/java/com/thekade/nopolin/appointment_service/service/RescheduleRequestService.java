package com.thekade.nopolin.appointment_service.service;

import com.thekade.nopolin.appointment_service.dto.CreateRescheduleRequestDto;
import com.thekade.nopolin.appointment_service.dto.RescheduleRequestDto;
import com.thekade.nopolin.appointment_service.entity.CourtHearing;
import com.thekade.nopolin.appointment_service.entity.RescheduleRequest;
import com.thekade.nopolin.appointment_service.entity.RescheduleStatus;
import com.thekade.nopolin.appointment_service.repository.CourtHearingRepository;
import com.thekade.nopolin.appointment_service.repository.RescheduleRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RescheduleRequestService {

    private final RescheduleRequestRepository rescheduleRequestRepository;
    private final CourtHearingRepository courtHearingRepository;

    public RescheduleRequestDto createRescheduleRequest(Long userId, CreateRescheduleRequestDto requestDto) {
        // Check if court hearing exists
        CourtHearing courtHearing = courtHearingRepository.findById(requestDto.getCourtHearingId())
                .orElseThrow(() -> new RuntimeException("Court hearing not found"));

        // Check if user owns the court case
        if (!courtHearing.getCourtCase().getUserId().equals(userId)) {
            throw new RuntimeException("You can only request reschedule for your own court cases");
        }

        // Check if there's already a pending request for this hearing
        List<RescheduleRequest> existingRequests = rescheduleRequestRepository.findByCourtHearingId(requestDto.getCourtHearingId());
        boolean hasPendingRequest = existingRequests.stream()
                .anyMatch(request -> request.getStatus() == RescheduleStatus.PENDING);
        
        if (hasPendingRequest) {
            throw new RuntimeException("There is already a pending reschedule request for this hearing");
        }

        RescheduleRequest rescheduleRequest = RescheduleRequest.builder()
                .userId(userId)
                .courtHearing(courtHearing)
                .requestedDate(requestDto.getRequestedDate())
                .reason(requestDto.getReason())
                .status(RescheduleStatus.PENDING)
                .build();

        RescheduleRequest savedRequest = rescheduleRequestRepository.save(rescheduleRequest);
        return mapToDto(savedRequest);
    }

    public List<RescheduleRequestDto> getUserRescheduleRequests(Long userId) {
        return rescheduleRequestRepository.findByUserId(userId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<RescheduleRequestDto> getPendingRescheduleRequests() {
        return rescheduleRequestRepository.findByStatus(RescheduleStatus.PENDING)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<RescheduleRequestDto> getUserRescheduleRequestsByStatus(Long userId, RescheduleStatus status) {
        return rescheduleRequestRepository.findByUserIdAndStatus(userId, status)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public Optional<RescheduleRequestDto> getRescheduleRequestById(Long id) {
        return rescheduleRequestRepository.findById(id)
                .map(this::mapToDto);
    }

    public RescheduleRequestDto approveRescheduleRequest(Long id, String adminNotes) {
        RescheduleRequest request = rescheduleRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reschedule request not found"));

        request.setStatus(RescheduleStatus.APPROVED);
        request.setAdminNotes(adminNotes);
        
        RescheduleRequest savedRequest = rescheduleRequestRepository.save(request);
        return mapToDto(savedRequest);
    }

    public RescheduleRequestDto rejectRescheduleRequest(Long id, String adminNotes) {
        RescheduleRequest request = rescheduleRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reschedule request not found"));

        request.setStatus(RescheduleStatus.REJECTED);
        request.setAdminNotes(adminNotes);
        
        RescheduleRequest savedRequest = rescheduleRequestRepository.save(request);
        return mapToDto(savedRequest);
    }

    private RescheduleRequestDto mapToDto(RescheduleRequest request) {
        return RescheduleRequestDto.builder()
                .id(request.getId())
                .userId(request.getUserId())
                .courtHearingId(request.getCourtHearing().getId())
                .requestedDate(request.getRequestedDate())
                .reason(request.getReason())
                .status(request.getStatus())
                .adminNotes(request.getAdminNotes())
                .createdAt(request.getCreatedAt())
                .updatedAt(request.getUpdatedAt())
                .build();
    }
}
