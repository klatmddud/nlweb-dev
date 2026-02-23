package com.nlweb.program.service;

import com.nlweb.program.entity.ProgramUser;
import com.nlweb.program.repository.ProgramUserRepository;
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
public class ProgramUserCommandService {

    private final ProgramUserRepository programUserRepository;

    public ProgramUser create(UUID userId, UUID programId) {

    }
}
