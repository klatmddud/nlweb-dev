package com.nlweb.user.dto.response;

import com.nlweb.user.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResponse(
    UUID id,
    String username,
    String fullName,
    String email,
    Integer batch,
    String session,
    Boolean isVocalAllowed,
    Boolean isAdmin,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    LocalDateTime createdAt,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    LocalDateTime updatedAt
) {
    
    /** 회원가입 응답용 - 전체 정보 포함 */
    public static UserResponse forRegistration(User user) {
        return new UserResponse(
            user.getId(),
            user.getUsername(),
            user.getFullName(),
            user.getEmail(),
            user.getBatch(),
            user.getSession().toString(),
            user.getIsVocalAllowed(),
            user.getIsAdmin(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
    
    /** 로그인 응답용 - 민감 정보 제외 */
    public static UserResponse forLogin(User user) {
        return new UserResponse(
            null, // ID 숨김
            user.getUsername(),
            user.getFullName(),
            user.getEmail(),
            user.getBatch(),
            user.getSession().toString(),
            user.getIsVocalAllowed(),
            user.getIsAdmin(),
            null, // 생성일 숨김
            null  // 수정일 숨김
        );
    }
    
    /** 프로필 조회용 - 개인 정보 포함 */
    public static UserResponse forProfile(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getBatch(),
                user.getSession().toString(),
                user.getIsVocalAllowed(),
                user.getIsAdmin(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    /** 프로필 조회용 - 공개용 */
    public static UserResponse forPublic(User user) {
        return new UserResponse(
                null,
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getBatch(),
                user.getSession().toString(),
                user.getIsVocalAllowed(),
                user.getIsAdmin(),
                null,
                null
        );
    }
}
