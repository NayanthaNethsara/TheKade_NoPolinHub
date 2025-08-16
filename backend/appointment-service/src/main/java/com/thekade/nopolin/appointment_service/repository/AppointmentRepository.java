package com.thekade.nopolin.appointment_service.repository;

import com.thekade.nopolin.appointment_service.entity.Appointment;
import com.thekade.nopolin.appointment_service.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    List<Appointment> findByUserId(Long userId);
    
    List<Appointment> findByLawyerId(Long lawyerId);
    
    List<Appointment> findByUserIdAndStatus(Long userId, AppointmentStatus status);
    
    List<Appointment> findByLawyerIdAndStatus(Long lawyerId, AppointmentStatus status);
    
    @Query("SELECT a FROM Appointment a WHERE a.lawyer.id = :lawyerId " +
           "AND a.appointmentDate BETWEEN :startDate AND :endDate " +
           "AND a.status IN ('PENDING', 'CONFIRMED')")
    List<Appointment> findConflictingAppointments(@Param("lawyerId") Long lawyerId,
                                                 @Param("startDate") LocalDateTime startDate,
                                                 @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate BETWEEN :startDate AND :endDate " +
           "AND a.status = 'CONFIRMED'")
    List<Appointment> findUpcomingAppointments(@Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate);
}
