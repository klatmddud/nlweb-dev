package com.nlweb.amho.entity;

import lombok.*;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;
import java.time.LocalDateTime;

@Entity
@Table(name = "amhos")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Amho implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "user_code", length = 8, nullable = false, unique = true, updatable = false)
    private String userCode;

    @Column(name = "admin_code", length = 8, nullable = false, unique = true, updatable = false)
    private String adminCode;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public Amho(String userCode, String adminCode, Boolean isActive) {
        this.userCode = userCode;
        this.adminCode = adminCode;
        this.isActive = isActive;
    }

    public void deactivate() {
        this.isActive = false;
    }

}
