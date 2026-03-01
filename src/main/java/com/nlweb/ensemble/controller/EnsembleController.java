package com.nlweb.ensemble.controller;

import com.nlweb.core.security.NlwebUserDetails;
import com.nlweb.ensemble.dto.request.*;
import com.nlweb.ensemble.dto.response.EnsembleResponse;
import com.nlweb.ensemble.facade.EnsembleFacade;
import com.nlweb.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ensembles")
public class EnsembleController {

    private final EnsembleFacade ensembleFacade;

    /** 새 합주곡 생성 (Program의 ensemble_apply 시간 참조) */
    @PostMapping
    public ResponseEntity<ApiResponse<EnsembleResponse>> createEnsemble(
            @AuthenticationPrincipal NlwebUserDetails principal,
            @RequestBody CreateEnsembleRequest request
    ) {
        EnsembleResponse response = ensembleFacade.createNewEnsemble(principal, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    /** 합주곡 정보 수정 (집부) */
    @PatchMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EnsembleResponse>> updateEnsemble(
            @RequestBody UpdateEnsembleRequest request
    ) {
        EnsembleResponse response = ensembleFacade.updateEnsemble(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(response));
    }

    /** 합주곡 삭제 (집부) */
    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteEnsemble(
            @RequestBody DeleteEnsembleRequest request
    ) {
        ensembleFacade.deleteEnsemble(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(null, "합주를 삭제했습니다."));
    }

    /** 합주에 세션 신청(Program의 session_apply 시간 참조) */
    @PatchMapping("/session")
    public ResponseEntity<ApiResponse<EnsembleResponse>> applySession(
            @AuthenticationPrincipal NlwebUserDetails principal,
            @Valid @RequestBody ApplySessionRequest request
    ) {
        EnsembleResponse response = ensembleFacade.applySession(principal, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(response));
    }

    /** 특정 프로그램의 합주곡 모두 조회 */
    @GetMapping()
    public ResponseEntity<ApiResponse<List<EnsembleResponse>>> getAllEnsemblesInProgram(
            @Valid @RequestBody GetAllEnsembleRequest request
    ) {
        List<EnsembleResponse> response = ensembleFacade.getAllEnsemblesInProgram(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(response));
    }

}
