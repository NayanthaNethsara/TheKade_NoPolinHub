package com.thekade.nopolin.appointment_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "court_cases")
public class CourtCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "case_number", nullable = false, unique = true)
    private String caseNumber;

    @Column(name = "user_id", nullable = false)
    private Long userId; // Reference to User entity in auth service

    @Column(name = "court_name")
    private String courtName;

    @Column(name = "case_type")
    private String caseType; // e.g., "CIVIL", "CRIMINAL", "FAMILY"

    @Column(name = "case_title")
    private String caseTitle;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @OneToMany(mappedBy = "courtCase", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CourtHearing> hearings;
}
