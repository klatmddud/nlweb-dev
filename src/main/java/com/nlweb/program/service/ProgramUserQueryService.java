package com.nlweb.program.service;

import com.nlweb.program.entity.*;
import com.nlweb.program.repository.*;
import com.nlweb.program.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProgramUserQueryService {

    private final ProgramUserRepository programUserRepository;

    /** 사용자 ID, 프로그램 ID로 찾기 */
    public ProgramUser getByUserIdAndProgramId(UUID userId, UUID programId) {
        return programUserRepository.findByUserIdAndProgramId(userId, programId)
                .orElseThrow(() -> new ProgramUserNotFoundException("userId: " + userId + " programId: " + programId));
    }

    public void checkIsUserOfProgram(UUID userId, UUID programId) {
        if (programUserRepository.findByUserIdAndProgramId(userId, programId).isEmpty()) {
            throw new ProgramUserNotFoundException("userId: " + userId + " programId: " + programId);
        }
    }

}
