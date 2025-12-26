package com.nlweb.amho.dto;

import com.nlweb.amho.entity.Amho;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AmhoObject {

    private UUID id;
    private String userCode;
    private String adminCode;
    private Boolean isActive;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    public static AmhoObject fromEntity(Amho entity) {
        return AmhoObject.builder()
                .id(entity.getId())
                .userCode(entity.getUserCode())
                .adminCode(entity.getAdminCode())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .build();
    }

}
