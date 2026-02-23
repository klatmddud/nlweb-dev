package com.nlweb.program.entity;

import lombok.*;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "programs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Program implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Setter
    @Column(name = "title", nullable = false)
    private String title;

    @Setter
    @Column(name = "program_apply_start_at")
    private LocalDateTime programApplyStartAt;

    @Setter
    @Column(name = "program_apply_end_at")
    private LocalDateTime programApplyEndAt;

    @Setter
    @Column(name = "ensemble_apply_start_at")
    private LocalDateTime ensembleApplyStartAt;

    @Setter
    @Column(name = "ensemble_apply_end_at")
    private LocalDateTime ensembleApplyEndAt;

    @Setter
    @Column(name = "session_apply_start_at")
    private LocalDateTime sessionApplyStartAt;

    @Setter
    @Column(name = "session_apply_end_at")
    private LocalDateTime sessionApplyEndAt;

    @Setter
    @Column(name = "timeslot_apply_start_at")
    private LocalDateTime timeslotApplyStartAt;

    @Setter
    @Column(name = "timeslot_apply_end_at")
    private LocalDateTime timeslotApplyEndAt;

    @CreatedDate
    @Column(name = "created_at",  nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Version
    @Column(name = "version")
    private Integer version;

    @Builder
    public Program(
            String title,
            LocalDateTime programApplyStartAt,
            LocalDateTime programApplyEndAt,
            LocalDateTime ensembleApplyStartAt,
            LocalDateTime ensembleApplyEndAt,
            LocalDateTime sessionApplyStartAt,
            LocalDateTime sessionApplyEndAt,
            LocalDateTime timeslotApplyStartAt,
            LocalDateTime timeslotApplyEndAt
    ) {
        this.title = title;
        this.programApplyStartAt = programApplyStartAt;
        this.programApplyEndAt = programApplyEndAt;
        this.ensembleApplyStartAt = ensembleApplyStartAt;
        this.ensembleApplyEndAt = ensembleApplyEndAt;
        this.sessionApplyStartAt = sessionApplyStartAt;
        this.sessionApplyEndAt = sessionApplyEndAt;
        this.timeslotApplyStartAt = timeslotApplyStartAt;
        this.timeslotApplyEndAt = timeslotApplyEndAt;
    }

}
