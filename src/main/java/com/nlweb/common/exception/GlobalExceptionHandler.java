package com.nlweb.common.exception;

import com.nlweb.auth.exception.*;
import com.nlweb.admin.exception.*;
import com.nlweb.user.exception.*;
import com.nlweb.amho.exception.*;
import com.nlweb.program.exception.*;
import com.nlweb.common.dto.ApiResponse;
import com.nlweb.common.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ========== Auth 관련 예외 ==========

    /** 잘못된 인증/인가 */
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidCredentials(
            InvalidCredentialsException ex, HttpServletRequest request) {
        log.warn("잘못된 자격증명: {}", ex.getMessage());
        return buildErrorResponse("INVALID_CREDENTIALS", ex.getMessage(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadCredentials(
            BadCredentialsException ex, HttpServletRequest request) {
        log.warn("인증 실패: {}", ex.getMessage());
        return buildErrorResponse("AUTHENTICATION_FAILED", "인증에 실패했습니다",
                HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Object>> handleAuthentication(
            AuthenticationException ex, HttpServletRequest request) {
        log.warn("인증 오류: {}", ex.getMessage());
        return buildErrorResponse("AUTHENTICATION_ERROR", "인증이 필요합니다",
                HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDenied(
            AccessDeniedException ex, HttpServletRequest request) {
        log.warn("접근 권한 없음: {} - User: {}", ex.getMessage(), SecurityUtils.getCurrentUsername());
        return buildErrorResponse("ACCESS_DENIED", "접근 권한이 없습니다",
                HttpStatus.FORBIDDEN, request);
    }

    // ========== Amho 관련 예외 ==========

    /** 유효하지 않은 Amho */
    @ExceptionHandler(InvalidAmhoException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidAmho(
            InvalidAmhoException ex, HttpServletRequest request) {
        log.warn("유효하지 않은 가입 코드: {}", ex.getMessage());
        return buildErrorResponse("INVALID_AMHO", ex.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    /** Amho를 찾을 수 없음 */
    @ExceptionHandler(AmhoNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleAmhoNotFound(
            AmhoNotFoundException ex, HttpServletRequest request) {
        log.warn("암호를 찾을 수 없습니다: {}", ex.getMessage());
        return buildErrorResponse("AMHO_NOT_FOUND", ex.getMessage(), HttpStatus.NOT_FOUND, request);
    }

    // ========== Admin 관련 예외 ==========

    @ExceptionHandler(AdminNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleAdminNotFound(
            AdminNotFoundException ex, HttpServletRequest request) {
        log.warn("관리자를 찾을 수 없읍: {}", ex.getMessage());
        return buildErrorResponse("ADMIN_NOT_FOUND", ex.getMessage(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(DuplicateAdminException.class)
    public ResponseEntity<ApiResponse<Object>> handleDuplicateAdmin(
            DuplicateAdminException ex, HttpServletRequest request) {
        log.warn("이미 존재하는 관리자: {}", ex.getMessage());
        return buildErrorResponse("DUPLICATE_ADMIN", ex.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    // ========== User 관련 예외 ==========

    /** 사용자를 찾을 수 없음 */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserNotFound(
            UserNotFoundException ex, HttpServletRequest request) {
        log.warn("사용자를 찾을 수 없음: {}", ex.getMessage());
        return buildErrorResponse("USER_NOT_FOUND", ex.getMessage(), HttpStatus.NOT_FOUND, request);
    }

    /** 중복된 사용자 정보 */
    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<ApiResponse<Object>> handleDuplicateUser(
            DuplicateUserException ex, HttpServletRequest request) {
        log.warn("중복 사용자 등록 시도: {}", ex.getMessage());
        return buildErrorResponse("DUPLICATE_USER", ex.getMessage(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(InvalidUserOperationException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidUserOperation(
            InvalidUserOperationException ex, HttpServletRequest request) {
        log.warn("잘못된 사용자 접근: {}", ex.getMessage());
        return buildErrorResponse("INVALID_USER_OPERATION", ex.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    // ========== Program 관련 예외 ==========

    @ExceptionHandler(ProgramNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleProgramNotFound(
            ProgramNotFoundException ex, HttpServletRequest request) {
        log.warn("프로그램을 찾을 수 없음: {}", ex.getMessage());
        return buildErrorResponse("PROGRAM_NOT_FOUND", ex.getMessage(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(ProgramUserNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleProgramUserNotFound(
            ProgramUserNotFoundException ex, HttpServletRequest request) {
        log.warn("프로그램 참가자를 찾을 수 없음: {}", ex.getMessage());
        return buildErrorResponse("PROGRAM_USER_NOT_FOUND", ex.getMessage(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(DuplicateProgramUserException.class)
    public ResponseEntity<ApiResponse<Object>> handleDuplicateProgramUser(
            DuplicateProgramUserException ex, HttpServletRequest request
    ) {
        log.warn("이미 존재하는 프로그램 참가자: {}", ex.getMessage());
        return buildErrorResponse("DUPLICATE_PROGRAM_USER", ex.getMessage(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(ProgramApplyNotAvailableException.class)
    public ResponseEntity<ApiResponse<Object>> handleProgramApplyNotAvailable(
            ProgramApplyNotAvailableException ex, HttpServletRequest request
    ) {
        log.warn("현재 프로그램 관련 신청이 가능하지 않음");
        return buildErrorResponse("PROGRAM_APPLY_NOT_AVAILABLE", ex.getMessage(), HttpStatus.CONFLICT, request);
    }

    // ========== 입력 검증 예외 ==========

    /** @Valid 검증 실패 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });

        String message = fieldErrors.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining(", "));

        log.warn("입력 검증 실패: {}", message);

        ApiResponse<Object> response = ApiResponse.error("VALIDATION_ERROR", "입력 데이터가 올바르지 않습니다");
        response.setData(fieldErrors);
        response.setTimestamp(LocalDateTime.now());
        response.setPath(request.getRequestURI());

        return ResponseEntity.badRequest().body(response);
    }

    /** 제약 조건 위반 */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleConstraintViolation(
            ConstraintViolationException ex, HttpServletRequest request) {

        String message = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));

        log.warn("제약 조건 위반: {}", message);
        return buildErrorResponse("CONSTRAINT_VIOLATION", message,
                HttpStatus.BAD_REQUEST, request);
    }

    /** 필수 파라미터 누락 */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Object>> handleMissingParams(
            MissingServletRequestParameterException ex, HttpServletRequest request) {

        String message = String.format("필수 파라미터가 누락되었습니다: %s (%s)",
                ex.getParameterName(), ex.getParameterType());
        log.warn("필수 파라미터 누락: {}", message);
        return buildErrorResponse("MISSING_PARAMETER", message,
                HttpStatus.BAD_REQUEST, request);
    }

    /** 타입 변환 실패 */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Object>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {

        String message = String.format("잘못된 파라미터 타입: %s (기대: %s, 입력: %s)",
                ex.getName(),
                ex.getRequiredType().getSimpleName(),
                ex.getValue());
        log.warn("타입 변환 실패: {}", message);
        return buildErrorResponse("TYPE_MISMATCH", message,
                HttpStatus.BAD_REQUEST, request);
    }

    // =========== HTTP 관련 예외 ===========

    /** 지원하지 않는 HTTP 메서드 */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {

        String supportedMethods = String.join(", ", ex.getSupportedMethods());
        String message = String.format("지원하지 않는 HTTP 메서드: %s (지원: %s)",
                ex.getMethod(), supportedMethods);
        log.warn("지원하지 않는 HTTP 메서드: {}", message);
        return buildErrorResponse("METHOD_NOT_SUPPORTED", message,
                HttpStatus.METHOD_NOT_ALLOWED, request);
    }

    /** 잘못된 JSON 형식 */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpServletRequest request) {

        String message = "잘못된 JSON 형식입니다";
        if (ex.getCause() != null) {
            message += ": " + ex.getCause().getMessage();
        }
        log.warn("잘못된 JSON 형식: {}", message);
        return buildErrorResponse("INVALID_JSON", message,
                HttpStatus.BAD_REQUEST, request);
    }

    /** 핸들러를 찾을 수 없음 (404) */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNoHandlerFound(
            NoHandlerFoundException ex, HttpServletRequest request) {

        String message = String.format("요청한 경로를 찾을 수 없습니다: %s %s",
                ex.getHttpMethod(), ex.getRequestURL());
        log.warn("핸들러를 찾을 수 없음: {}", message);
        return buildErrorResponse("NOT_FOUND", message,
                HttpStatus.NOT_FOUND, request);
    }

    // =========== 데이터베이스 관련 예외 ===========

    /** 데이터 무결성 위반 */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataIntegrityViolation(
            DataIntegrityViolationException ex, HttpServletRequest request) {

        String message = "데이터 무결성 제약 조건을 위반했습니다";

        if (ex.getMessage() != null) {
            if (ex.getMessage().contains("Duplicate entry")) {
                message = "중복된 데이터입니다";
            } else if (ex.getMessage().contains("foreign key constraint")) {
                message = "참조된 데이터가 존재하지 않습니다";
            } else if (ex.getMessage().contains("not-null")) {
                message = "필수 데이터가 누락되었습니다";
            }
        }

        log.error("데이터 무결성 위반: {}", ex.getMessage());
        return buildErrorResponse("DATA_INTEGRITY_VIOLATION", message,
                HttpStatus.CONFLICT, request);
    }

    // =========== 일반적인 예외 ===========

    /** IllegalArgumentException */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgument(
            IllegalArgumentException ex, HttpServletRequest request) {

        log.warn("잘못된 인수: {}", ex.getMessage());
        return buildErrorResponse("INVALID_ARGUMENT", ex.getMessage(),
                HttpStatus.BAD_REQUEST, request);
    }

    /** IllegalStateException */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalState(
            IllegalStateException ex, HttpServletRequest request) {

        log.error("잘못된 상태: {}", ex.getMessage());
        return buildErrorResponse("INVALID_STATE", ex.getMessage(),
                HttpStatus.CONFLICT, request);
    }

    // ========== 최종 예외 처리기 ==========

    /** INTERNAL_SERVER_ERROR */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleAllExceptions(
            Exception ex, HttpServletRequest request) {
        String errorId = java.util.UUID.randomUUID().toString();
        log.error("서버 내부 오류 [{}]: {}",  errorId, ex.getMessage(), ex);

        String message = "서버 내부 오류가 발생했습니다. 문제가 지속되면 관리자에게 문의하세요. (오류 ID: " + errorId + ")";
        return buildErrorResponse("INTERNAL_SERVER_ERROR", message, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }


    // ============ 헬퍼 ============

    /** 에러 응답 빌드 */
    private ResponseEntity<ApiResponse<Object>> buildErrorResponse(
            String errorCode, String message, HttpStatus status, HttpServletRequest request) {
        ApiResponse<Object> response = ApiResponse.error(errorCode, message);
        response.setTimestamp(LocalDateTime.now());
        response.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(response);
    }

}
