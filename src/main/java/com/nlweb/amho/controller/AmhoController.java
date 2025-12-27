package com.nlweb.amho.controller;

import com.nlweb.amho.dto.AmhoObject;
import com.nlweb.amho.facade.AmhoFacade;
import com.nlweb.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/api/amhos")
@RequiredArgsConstructor
@Validated
public class AmhoController {

    private final AmhoFacade amhoFacade;

    /** 현재 인증 코드 반환 */
    @GetMapping
    public ResponseEntity<ApiResponse<AmhoObject>> getCurrentAmho(
            HttpServletRequest httpRequest) {
        AmhoObject response = amhoFacade.getActiveAmho();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(response));
    }

    /** 현재 인증 코드 리셋 */
    @PostMapping
    public ResponseEntity<ApiResponse<AmhoObject>> resetCurrentAmho(
            HttpServletRequest httpRequest) {
        AmhoObject response = amhoFacade.resetAmho();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }
}
