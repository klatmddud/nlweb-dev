package com.nlweb.amho.controller;

import com.nlweb.core.security.NlwebUserDetails;
import com.nlweb.amho.dto.AmhoResponse;
import com.nlweb.amho.facade.AmhoFacade;
import com.nlweb.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import jakarta.servlet.http.HttpServletRequest;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/amhos")
@RequiredArgsConstructor
public class AmhoController {

    private final AmhoFacade amhoFacade;

    /**
     * GET
     * /api/amhos
     * 현재 인증 코드 반환 */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AmhoResponse>> getCurrentAmho(
            @AuthenticationPrincipal NlwebUserDetails principal,
            HttpServletRequest httpRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(amhoFacade.getActiveAmho(
                        principal.getUserId(),
                        principal.getUsername()
                )));
    }

    /**
     *  POST
     *  /api/amhos
     *  현재 인증 코드 리셋 */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AmhoResponse>> resetCurrentAmho(
            @AuthenticationPrincipal NlwebUserDetails principal,
            HttpServletRequest httpRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(amhoFacade.resetAmho(
                        principal.getUserId(),
                        principal.getUsername()
                )));
    }
}
