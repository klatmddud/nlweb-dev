package com.nlweb.admin.dto.response;

import com.nlweb.admin.entity.Admin;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AdminResponse(
        UUID id,
        String username,
        String role,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        LocalDateTime createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        LocalDateTime updatedAt
) {

    public static AdminResponse forProfile(Admin admin) {
        return new AdminResponse(
                admin.getId(),
                admin.getUser().getUsername(),
                admin.getRole(),
                admin.getCreatedAt(),
                admin.getUpdatedAt()
        );
    }

}
