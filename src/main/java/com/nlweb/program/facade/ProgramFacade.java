package com.nlweb.program.facade;

import com.nlweb.core.security.NlwebUserDetails;
import com.nlweb.program.entity.*;
import com.nlweb.program.dto.request.*;
import com.nlweb.program.dto.response.*;
import com.nlweb.program.exception.ProgramApplyNotAvailableException;
import com.nlweb.program.service.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProgramFacade {

    private final ProgramQueryService programQueryService;
    private final ProgramCommandService programCommandService;
    private final ProgramUserCommandService programUserCommandService;
    private final ProgramUserQueryService programUserQueryService;

    /** 새 프로그램 생성 */
    public ProgramResponse createNewProgram() {
        return ProgramResponse.forPublic(
                programCommandService.create()
        );
    }

    /** 프로그램 정보 수정 */
    public ProgramResponse updateProgram(UpdateProgramRequest request) {
        return ProgramResponse.forPublic(
                programCommandService.update(request)
        );
    }

    /** 프로그램 삭제 */
    public void deleteProgram(UUID id) {
        // 1. 해당 프로그램 유저들 모두 삭제
        // 2. 해당 프로그램에 등록된 ensemble
        programCommandService.delete(id);
    }

    /** 프로그램 참여자 생성 */
    public ProgramUserResponse createNewProgramUser(NlwebUserDetails principal, CreateProgramUserRequest request) {

        if (!programQueryService.isProgramApplyAvailable(request.programId())) {
            throw new ProgramApplyNotAvailableException("현재 프로그램 참가 신청이 불가합니다.");
        }

        return ProgramUserResponse.forPublic(
                programUserCommandService.create(principal.getUserId(), request.programId())
        );
    }

    /** 프로그램 참여자 삭제 */
    public void deleteProgramUser(NlwebUserDetails principal, DeleteProgramUserRequest request) {
        UUID programUserId = programUserQueryService.getByUserIdAndProgramId(principal.getUserId(), request.programId()).getId();
        programUserCommandService.delete(programUserId);
    }

    /** 내가 참여 중인 프로그램 조회 */

}
