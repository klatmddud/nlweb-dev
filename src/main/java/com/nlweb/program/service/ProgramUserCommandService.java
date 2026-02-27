package com.nlweb.program.service;

import com.nlweb.user.entity.User;
import com.nlweb.user.repository.UserRepository;
import com.nlweb.user.exception.UserNotFoundException;
import com.nlweb.program.entity.*;
import com.nlweb.program.repository.*;
import com.nlweb.program.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProgramUserCommandService {

    private final UserRepository userRepository;
    private final ProgramUserRepository programUserRepository;
    private final ProgramRepository programRepository;

    /** 새 프로그램 참여자 생성 */
    public ProgramUser create(UUID userId, UUID programId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("id: " + userId));
        Program program = programRepository.findById(programId).orElseThrow(() -> new ProgramNotFoundException("id: " + programId));

        if (programUserRepository.findByUserIdAndProgramId(user.getId(), programId).isPresent()) {
            throw new DuplicateProgramUserException("userId: " + userId + " programId: " + programId);
        }

        ProgramUser programUser = ProgramUser.builder()
                .user(user)
                .program(program)
                .build();

        ProgramUser newProgramUser = programUserRepository.save(programUser);

        log.info("새 프로그램 참가자 생성 완료: userId = {}, programId = {}", userId, programId);

        return newProgramUser;
    }

    /** 프로그램 참가자 삭제 */
    public ProgramUser delete(UUID id) {
        ProgramUser programUser = programUserRepository.findById(id).orElseThrow(() -> new ProgramUserNotFoundException("id: " + id));

        programUserRepository.delete(programUser);

        log.info("프로그램 참가자 삭제 완료: id = {}", id);

        return programUser;
    }

    /** 사용자 ID로 모든 프로그램 참가 제거 */
    public void deleteAllByUserId(UUID userId) {
        List<ProgramUser> programUsers = programUserRepository.findByUserId(userId);
        programUserRepository.deleteAll(programUsers);
    }
}
