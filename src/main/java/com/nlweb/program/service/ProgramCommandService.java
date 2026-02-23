package com.nlweb.program.service;

import com.nlweb.program.entity.Program;
import com.nlweb.program.repository.ProgramRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private String generateNextProgramTitle() {
        if (!programRepository.existsByTitle(BASE_TITLE)) {
            return BASE_TITLE;
        }

        int suffix = 1;
        while (programRepository.existsByTitle(BASE_TITLE + " " + suffix)) {
            suffix++;
        }
        return BASE_TITLE + " " + suffix;
    }

}
