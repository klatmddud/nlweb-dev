package com.nlweb.program.service;

import com.nlweb.program.exception.ProgramNotFoundException;
import com.nlweb.program.repository.ProgramRepository;
import com.nlweb.program.entity.Program;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProgramQueryService {

    private final ProgramRepository programRepository;

    /** ID로 찾기 */
    public Program getById(UUID id) {
        return programRepository.findById(id).orElseThrow(() -> new ProgramNotFoundException("id: " + id));
    }

    /** 현재 프로그램 신청이 가능한지 */
    public boolean isProgramApplyAvailable(UUID programId) {
        Program program = getById(programId);
        return isApplyAvailable(program.getProgramApplyStartAt(), program.getProgramApplyEndAt());
    }

    /** 현재 합주 신청이 가능한지 */
    public boolean isEnsembleApplyAvailable(UUID programId) {
        Program program = getById(programId);
        return isApplyAvailable(program.getEnsembleApplyStartAt(), program.getEnsembleApplyEndAt());
    }

    /** 현재 세션 신청이 가능한지 */
    public boolean isSessionApplyAvailable(UUID programId) {
        Program program = getById(programId);
        return isApplyAvailable(program.getSessionApplyStartAt(), program.getSessionApplyEndAt());
    }

    /** 현재 시간표 신청이 가능한지 */
    public boolean isTimeslotApplyAvailable(UUID programId) {
        Program program = getById(programId);
        return isApplyAvailable(program.getTimeslotApplyStartAt(), program.getTimeslotApplyEndAt());
    }

    /** 현재 시각이 신청 가능 구간에 포함되는지 확인 */
    private boolean isApplyAvailable(LocalDateTime startAt, LocalDateTime endAt) {
        LocalDateTime now = LocalDateTime.now();

        if (startAt == null && endAt == null) {
            return false;
        }

        if (startAt == null) {
            return now.isBefore(endAt);
        }

        if (endAt == null) {
            return !now.isBefore(startAt);
        }

        return !now.isBefore(startAt) && now.isBefore(endAt);
    }

}
