package com.nlweb.admin.controller;

import com.nlweb.admin.dto.request.CreateAdminRequest;
import com.nlweb.admin.dto.request.UpdateMyRoleRequest;
import com.nlweb.admin.dto.response.AdminResponse;
import com.nlweb.admin.facade.AdminFacade;
import com.nlweb.common.dto.ApiResponse;
import com.nlweb.core.security.NlwebUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admins")
public class AdminController {

    private final AdminFacade adminFacade;

    /**
     * POST
     * /api/admins
     * 집부 권한 부여
     */
    @PostMapping
    public ResponseEntity<ApiResponse<AdminResponse>> getAdminAuthority(
            @AuthenticationPrincipal NlwebUserDetails principal,
            @RequestBody CreateAdminRequest request
    ) {
        AdminResponse response = adminFacade.grantAdminAuthority(principal, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    /**
     * GET
     * /api/admins
     * 전체 관리자 조회
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<AdminResponse>>> getAllAdmins(
            @AuthenticationPrincipal NlwebUserDetails principal
    ) {
        List<AdminResponse> response = adminFacade.getAllAdmins(principal);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(response));
    }

    /**
     * PATCH
     * /api/admins/me/role
     * 내 집부 역할 수정
     */
    @PatchMapping("/me")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AdminResponse>> updateMyRole(
            @Valid @RequestBody UpdateMyRoleRequest requestBody,
            @AuthenticationPrincipal NlwebUserDetails principal
    ) {
        AdminResponse response = adminFacade.updateMyRole(requestBody, principal);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(response));
    }

    /**
     * DELETE
     * /api/admins/me
     * 내 집부 권한 제거
     */
    @DeleteMapping("/me")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> revokeMyAuthority(
            @AuthenticationPrincipal NlwebUserDetails principal
    ) {
        adminFacade.revokeMyAuthority(principal);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(null, "집부 권한 박탈 완료"));
    }

}
