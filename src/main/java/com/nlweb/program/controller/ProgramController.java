package com.nlweb.program.controller;

import com.nlweb.core.security.NlwebUserDetails;
import com.nlweb.program.dto.request.CreateProgramUserRequest;
import com.nlweb.program.dto.request.DeleteProgramUserRequest;
import com.nlweb.program.dto.request.UpdateProgramRequest;
import com.nlweb.program.facade.ProgramFacade;
import com.nlweb.program.dto.response.*;
import com.nlweb.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/programs")
public class ProgramController {

    private final ProgramFacade programFacade;

    /** 새 프로그램 생성 (집부 권한) */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProgramResponse>> createProgram(
            @AuthenticationPrincipal NlwebUserDetails principal
    ) {
        ProgramResponse response = programFacade.createNewProgram();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    /** 프로그램 수정 (집부 권한) */
    @PatchMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProgramResponse>> updateProgram(
            @AuthenticationPrincipal NlwebUserDetails principal,
            @RequestBody UpdateProgramRequest request
    ) {
        ProgramResponse response = programFacade.updateProgram(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(response));
    }

    /** 프로그램 삭제 (집부 권한) */

    /** 프로그램 참여 신청 */
    @PostMapping("/apply")
    public ResponseEntity<ApiResponse<ProgramUserResponse>> applyProgram(
            @AuthenticationPrincipal NlwebUserDetails principal,
            @RequestBody CreateProgramUserRequest request
    ) {
        ProgramUserResponse response = programFacade.createNewProgramUser(principal, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    /** 프로그램 참여 취소 */
    public ResponseEntity<ApiResponse<ProgramUserResponse>> cancelProgramApplication(
            @AuthenticationPrincipal NlwebUserDetails principal,
            @RequestBody DeleteProgramUserRequest request
    ) {
        programFacade.deleteProgramUser(principal, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(null, "참가 취소 완료"));
    }

    /** 내가 참여 중인 프로그램 조회 */


}
