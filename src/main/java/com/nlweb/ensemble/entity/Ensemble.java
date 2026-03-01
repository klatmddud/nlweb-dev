package com.nlweb.ensemble.entity;

import com.nlweb.program.entity.Program;
import lombok.*;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.util.UUID;

@Entity
@Table(name = "ensembles")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Ensemble implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", nullable = false, updatable = false)
    private Program program;

    @Setter
    @Column(name = "artist", nullable = false)
    private String artist;

    @Setter
    @Column(name = "title", nullable = false)
    private String title;

    @Setter
    @Column(name = "vocal")
    private String vocal;

    @Setter
    @Column(name = "lead_guitar")
    private String leadGuitar;

    @Setter
    @Column(name = "rhythm_guitar")
    private String rhythmGuitar;

    @Setter
    @Column(name = "bass")
    private String bass;

    @Setter
    @Column(name = "drum")
    private String drum;

    @Setter
    @Column(name = "piano")
    private String piano;

    @Setter
    @Column(name = "synth")
    private String synth;

    @Setter
    @Column(name = "etc")
    private String etc;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week")
    private DayOfWeek dayOfWeek;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

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
    public Ensemble(
            Program program,
            String artist,
            String title,
            String vocal,
            String leadGuitar,
            String rhythmGuitar,
            String bass,
            String drum,
            String piano,
            String synth,
            String etc
    ) {
        this.program = program;
        this.artist = artist;
        this.title = title;
        this.vocal = vocal;
        this.leadGuitar = leadGuitar;
        this.rhythmGuitar = rhythmGuitar;
        this.bass = bass;
        this.drum = drum;
        this.piano = piano;
        this.synth = synth;
        this.etc = etc;
        this.dayOfWeek = null;
        this.startTime = null;
        this.endTime = null;
    }

}
