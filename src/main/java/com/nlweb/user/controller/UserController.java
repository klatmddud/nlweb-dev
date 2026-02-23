package com.nlweb.user.controller;

import com.nlweb.core.security.NlwebUserDetails;
import com.nlweb.user.facade.UserFacade;
import com.nlweb.user.dto.request.*;
import com.nlweb.user.dto.response.UserResponse;
import com.nlweb.user.enums.UserSessionType;
import com.nlweb.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserFacade userFacade;

    /**
     * GET
     * /api/users/me
     * 내 정보 조회 */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMyProfile(
            @AuthenticationPrincipal NlwebUserDetails principal
    ) {
        UserResponse response = userFacade.getProfile(principal.getUserId(), principal.getUsername());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(response));
    }

    /**
     * PATCH
     * /api/users/me
     * 내 정보 수정 */
    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateMyProfile(
            @AuthenticationPrincipal NlwebUserDetails principal,
            @RequestBody UpdateUserRequest requestBody
    ) {
        UserResponse response = userFacade.updateProfile(principal.getUserId(), principal.getUsername(), requestBody);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(response));
    }

    /**
     * DELETE
     * /api/users/me
     * 회원 탈퇴 */
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> deleteMyProfile(
            @AuthenticationPrincipal NlwebUserDetails principal
    ) {
        UserResponse response = userFacade.deleteUser(principal.getUserId(), principal.getUsername());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(response));
    }

    /**
     * GET
     * /api/users?session=?&batch=?
     * 세션, 기수로 사용자 검색 */
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> searchUsers(
            @RequestParam(required = false) String session,
            @RequestParam(required = false) Integer batch,
            @AuthenticationPrincipal NlwebUserDetails principal
    ) {
        List<UserResponse> response = userFacade.searchUsers(
                UserSessionType.fromString(session),
                batch,
                principal.getUserId(),
                principal.getUsername()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(response));
    }

}
