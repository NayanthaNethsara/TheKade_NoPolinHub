package com.thekade.nopolin.appointment_service.repository;

import com.thekade.nopolin.appointment_service.entity.RescheduleRequest;
import com.thekade.nopolin.appointment_service.entity.RescheduleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RescheduleRequestRepository extends JpaRepository<RescheduleRequest, Long> {
    
    List<RescheduleRequest> findByUserId(Long userId);
    
    List<RescheduleRequest> findByStatus(RescheduleStatus status);
    
    List<RescheduleRequest> findByUserIdAndStatus(Long userId, RescheduleStatus status);
    
    List<RescheduleRequest> findByCourtHearingId(Long courtHearingId);
}
