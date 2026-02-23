package com.nlweb.admin.dto.object;

import com.nlweb.admin.entity.Admin;
import com.nlweb.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminObject {

    private UUID id;
    private User user;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public static AdminObject fromEntity(Admin admin) {
        return AdminObject.builder()
                .id(admin.getId())
                .user(admin.getUser())
                .role(admin.getRole())
                .createdAt(admin.getCreatedAt())
                .updatedAt(admin.getUpdatedAt())
                .build();
    }

}
