package com.thekade.nopolin.appointment_service.repository;

import com.thekade.nopolin.appointment_service.entity.CourtCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourtCaseRepository extends JpaRepository<CourtCase, Long> {
    
    Optional<CourtCase> findByCaseNumber(String caseNumber);
    
    List<CourtCase> findByUserId(Long userId);
    
    List<CourtCase> findByUserIdAndIsActiveTrue(Long userId);
    
    Optional<CourtCase> findByCaseNumberAndUserId(String caseNumber, Long userId);
}
