package com.nlweb.program.service;

import com.nlweb.program.entity.Program;
import com.nlweb.program.repository.ProgramRepository;
import com.nlweb.program.dto.request.UpdateProgramRequest;
import com.nlweb.program.exception.ProgramNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProgramCommandService {

    private static final String BASE_TITLE = "새 프로그램";

    private final ProgramRepository programRepository;

    /** 새 프로그램 생성 */
    public Program create() {
        String generatedTitle = generateNextProgramTitle();

        Program program = Program.builder()
                .title(generatedTitle)
                .programApplyStartAt(null)
                .programApplyEndAt(null)
                .ensembleApplyStartAt(null)
                .ensembleApplyEndAt(null)
                .sessionApplyStartAt(null)
                .sessionApplyEndAt(null)
                .timeslotApplyStartAt(null)
                .timeslotApplyEndAt(null)
                .build();

        Program savedProgram = programRepository.save(program);
        log.info("새 프로그램 생성 완료: id={}, title={}", savedProgram.getId(), savedProgram.getTitle());

        return savedProgram;
    }

    /** 프로그램 정보 수정 */
    public Program update(UUID id, UpdateProgramRequest request) {
        Program program = programRepository.findById(id).orElseThrow(() -> new ProgramNotFoundException("id: " + id));

        if (request.title() != null) {
            program.setTitle(request.title());
        }

        // 1. 시작 시각이 존재할 경우 종료 시각은 무조건 시작 시각 이후
        // 2. 시작 시각과 종료 시각 중 하나 이상의 값이 null 가능
        // 2-1. 이 경우 (시작 시각, 종료 시각) = (null, 종료 시각) or (시작 시각, null) or (null, null)

        validateTimeRange(request.programApplyStartAt(), request.programApplyEndAt());
        validateTimeRange(request.ensembleApplyStartAt(), request.ensembleApplyEndAt());
        validateTimeRange(request.sessionApplyStartAt(), request.sessionApplyEndAt());
        validateTimeRange(request.timeslotApplyStartAt(), request.timeslotApplyEndAt());

        program.setProgramApplyStartAt(request.programApplyStartAt());
        program.setProgramApplyEndAt(request.programApplyEndAt());
        program.setEnsembleApplyStartAt(request.ensembleApplyStartAt());
        program.setEnsembleApplyEndAt(request.ensembleApplyEndAt());
        program.setSessionApplyStartAt(request.sessionApplyStartAt());
        program.setSessionApplyEndAt(request.sessionApplyEndAt());
        program.setTimeslotApplyStartAt(request.timeslotApplyStartAt());
        program.setTimeslotApplyEndAt(request.timeslotApplyEndAt());

        Program savedProgram = programRepository.save(program);

        log.info("프로그램 정보 수정: {}", savedProgram.getId());

        return savedProgram;
    }

    /** 프로그램 삭제 */
    public Program delete(UUID id) {
        Program program = programRepository.findById(id).orElseThrow(() -> new ProgramNotFoundException("id: " + id));

        programRepository.delete(program);

        log.info("프로그램 삭제 완료: {}", program.getId());

        return program;
    }

    /** 신청 시간 유효성 검사 */
    private void validateTimeRange(LocalDateTime startAt, LocalDateTime endAt) {
        if (startAt == null || endAt == null) {
            return;
        }

        if (!endAt.isAfter(startAt)) {
            throw new IllegalArgumentException(
                    "신청 종료 시각은 신청 시작 시각보다 나중이어야 합니다."
            );
        }
    }

    /** 제목 중복 검사 후 새 기본 제목 생성 */
    private String generateNextProgramTitle() {
        if(programRepository.findByTitle(BASE_TITLE).isEmpty()) {
            return BASE_TITLE;
        }

        int suffix = 1;
        while (programRepository.findByTitle(BASE_TITLE + " " + suffix).isPresent()) {
            suffix++;
        }

        return BASE_TITLE + " " + suffix;
    }

}
