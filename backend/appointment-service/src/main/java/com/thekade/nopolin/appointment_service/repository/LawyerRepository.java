package com.thekade.nopolin.appointment_service.repository;

import com.thekade.nopolin.appointment_service.entity.Lawyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LawyerRepository extends JpaRepository<Lawyer, Long> {
    
    Optional<Lawyer> findByUserId(Long userId);
    
    List<Lawyer> findByIsVerifiedTrueAndIsActiveTrue();
    
    List<Lawyer> findBySpecializationAndIsVerifiedTrueAndIsActiveTrue(String specialization);
    
    List<Lawyer> findByCityAndIsVerifiedTrueAndIsActiveTrue(String city);
    
    @Query("SELECT l FROM Lawyer l WHERE l.isVerified = true AND l.isActive = true " +
           "AND (:specialization IS NULL OR l.specialization = :specialization) " +
           "AND (:city IS NULL OR l.city = :city)")
    List<Lawyer> findAvailableLawyers(@Param("specialization") String specialization, 
                                     @Param("city") String city);
    
    List<Lawyer> findByIsFreeConsultationAvailableTrueAndIsVerifiedTrueAndIsActiveTrue();
}
