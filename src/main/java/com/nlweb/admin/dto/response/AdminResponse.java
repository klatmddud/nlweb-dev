package com.nlweb.admin.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.nlweb.admin.dto.object.AdminObject;

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

    public static AdminResponse forUpdateRole(AdminObject adminObject) {
        return from(adminObject);
    }

    public static AdminResponse from(AdminObject adminObject) {
        return new AdminResponse(
                adminObject.getId(),
                adminObject.getUser().getUsername(),
                adminObject.getRole(),
                adminObject.getCreatedAt(),
                adminObject.getUpdatedAt()
        );
    }
}
