package com.nlweb.admin.controller;

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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admins")
public class AdminController {

    private final AdminFacade adminFacade;

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
    @PatchMapping("/me/role")
    public ResponseEntity<ApiResponse<AdminResponse>> updateMyRole(
            @Valid @RequestBody UpdateMyRoleRequest requestBody,
            @AuthenticationPrincipal NlwebUserDetails principal
    ) {
        AdminResponse response = adminFacade.updateMyRole(requestBody, principal);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(response));
    }
}
