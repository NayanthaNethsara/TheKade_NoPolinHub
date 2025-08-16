package com.thekade.nopolin.appointment_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "court_hearings")
public class CourtHearing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "court_case_id", nullable = false)
    private CourtCase courtCase;

    @Column(name = "hearing_date", nullable = false)
    private LocalDateTime hearingDate;

    @Column(name = "hearing_type")
    private String hearingType; // e.g., "TRIAL", "PRE_TRIAL", "SENTENCING"

    @Column(name = "court_room")
    private String courtRoom;

    @Column(name = "judge_name")
    private String judgeName;

    @Column(name = "status")
    private String status = "SCHEDULED"; // SCHEDULED, COMPLETED, CANCELLED, POSTPONED

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}
