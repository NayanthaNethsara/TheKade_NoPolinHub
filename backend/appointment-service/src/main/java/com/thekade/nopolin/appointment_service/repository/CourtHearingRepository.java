package com.thekade.nopolin.appointment_service.repository;

import com.thekade.nopolin.appointment_service.entity.CourtHearing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CourtHearingRepository extends JpaRepository<CourtHearing, Long> {
    
    List<CourtHearing> findByCourtCaseId(Long courtCaseId);
    
    @Query("SELECT ch FROM CourtHearing ch WHERE ch.courtCase.userId = :userId " +
           "AND ch.hearingDate >= :startDate ORDER BY ch.hearingDate")
    List<CourtHearing> findUpcomingHearingsByUserId(@Param("userId") Long userId,
                                                   @Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT ch FROM CourtHearing ch WHERE ch.hearingDate BETWEEN :startDate AND :endDate " +
           "AND ch.status = 'SCHEDULED'")
    List<CourtHearing> findScheduledHearingsInDateRange(@Param("startDate") LocalDateTime startDate,
                                                        @Param("endDate") LocalDateTime endDate);
}
