package com.nlweb.amho.dto;

import com.nlweb.amho.entity.Amho;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AmhoResponse(
    UUID id,
    String userCode,
    String adminCode,
    Boolean isActive,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    LocalDateTime createdAt,
    String message
) {
    
    /** 현재 Amho 조회용 - 전체 정보 포함 */
    public static AmhoResponse forGet(Amho amho) {
        return new AmhoResponse(
            amho.getId(),
            amho.getUserCode(),
            amho.getAdminCode(),
            amho.getIsActive(),
            amho.getCreatedAt(),
            null
        );
    }
    
    /** Amho 리셋용 - 성공 메시지 포함 */
    public static AmhoResponse forReset(Amho amho) {
        return new AmhoResponse(
            amho.getId(),
            amho.getUserCode(),
            amho.getAdminCode(),
            amho.getIsActive(),
            amho.getCreatedAt(),
            "Amho 코드가 성공적으로 재설정되었습니다."
        );
    }
    
    /** 공개용 - 민감 정보 제외 */
    public static AmhoResponse forPublic(Amho amho) {
        return new AmhoResponse(
            null, // ID 숨김
            amho.getUserCode(),
            null, // adminCode 숨김
            amho.getIsActive(),
            null, // createdAt 숨김
            null
        );
    }
}