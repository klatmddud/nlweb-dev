package com.nlweb.user.entity;

import com.nlweb.user.enums.UserSessionType;
import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "username", length = 100, unique = true, nullable = false, updatable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Setter
    @Column(name = "full_name", length = 100, nullable = false)
    private String fullName;

    @Setter
    @Column(name = "email", length = 100, unique = true, nullable = false)
    private String email;

    @Setter
    @Column(name = "batch", nullable = false)
    private Integer batch;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "session", nullable = false, length = 10)
    private UserSessionType session;

    @Setter
    @ColumnDefault("false")
    @Column(name = "is_vocal_allowed", nullable = false)
    private Boolean isVocalAllowed = false;

    @Setter
    @ColumnDefault("false")
    @Column(name = "is_admin", nullable = false)
    private Boolean isAdmin = false;

    @CreatedDate
    @Column(name = "created_at",  nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Version
    @Column(name = "version")
    private Integer version;

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public String getPasswordHash() {
        return this.password;
    }

    @Builder
    public User(
            String username,
            String password,
            String fullName,
            String email,
            Integer batch,
            UserSessionType session,
            Boolean isVocalAllowed,
            Boolean isAdmin
    ) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.batch = batch;
        this.session = session;
        this.isVocalAllowed = session != null && session.equals(UserSessionType.VOCAL) || Boolean.TRUE.equals(isVocalAllowed);
        this.isAdmin = Boolean.TRUE.equals(isAdmin);
    }

    public boolean isAdmin() {
        return this.isAdmin != null && this.isAdmin;
    }

    public boolean isVocalSession() {
        return this.session.equals(UserSessionType.VOCAL);
    }

}
