package com.nlweb.timeslot.entity;

import com.nlweb.user.entity.User;
import com.nlweb.ensemble.entity.Ensemble;
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
@Table(name = "timeslots")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Timeslot implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ensemble_id", updatable = false)
    private Ensemble ensemble;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", updatable = false)
    private User user;

    @Setter
    @Column(name = "description")
    private String description;

    @Setter
    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @Setter
    @Column(name = "end_at", nullable = false)
    private LocalDateTime endAt;

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
    public Timeslot(
            Ensemble ensemble,
            User user,
            String description,
            LocalDateTime startAt,
            LocalDateTime endAt
    ) {
        this.ensemble = ensemble;
        this.user = user;
        this.description = description;
        this.startAt = startAt;
        this.endAt = endAt;
    }
}
