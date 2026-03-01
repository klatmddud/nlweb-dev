package com.nlweb.ensemble.facade;

import com.nlweb.core.security.NlwebUserDetails;
import com.nlweb.ensemble.exception.DuplicateEnsembleException;
import com.nlweb.ensemble.exception.DuplicateSessionException;
import com.nlweb.ensemble.service.*;
import com.nlweb.ensemble.dto.response.EnsembleResponse;
import com.nlweb.ensemble.dto.request.*;
import com.nlweb.ensemble.entity.Ensemble;
import com.nlweb.program.service.ProgramQueryService;
import com.nlweb.program.service.ProgramUserQueryService;
import com.nlweb.program.exception.ProgramApplyNotAvailableException;
import com.nlweb.program.entity.Program;
import com.nlweb.timeslot.service.TimeslotCommandService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EnsembleFacade {

    private final EnsembleQueryService ensembleQueryService;
    private final EnsembleCommandService ensembleCommandService;
    private final ProgramQueryService programQueryService;
    private final ProgramUserQueryService programUserQueryService;
    private final TimeslotCommandService timeslotCommandService;

    /** 새 합주 생성 */
    public EnsembleResponse createNewEnsemble(NlwebUserDetails principal, CreateEnsembleRequest request) {
        // 프로그램 존재 유무 확인
        Program program = programQueryService.getById(request.program_id());

        // 유저가 프로그램에 참여 중인지 확인
        programUserQueryService.checkIsUserOfProgram(principal.getUserId(), program.getId());

        // 신청 기간인지 확인
        if (!programQueryService.isEnsembleApplyAvailable(program.getId())) {
            throw new ProgramApplyNotAvailableException(program.getId().toString());
        }

        // 같은 곡이 이미 있는지 확인
        if (ensembleQueryService.isArtistAndTitleExists(request.artist(), request.title())) {
            throw new DuplicateEnsembleException("이미 존재하는 아티스트와 제목입니다.");
        }

        return EnsembleResponse.forPublic(
                ensembleCommandService.create(program, request)
        );
    }

    /** 합주 수정 */
    public EnsembleResponse updateEnsemble(UpdateEnsembleRequest request) {
        Ensemble ensemble = ensembleQueryService.getById(request.id());

        return EnsembleResponse.forPublic(
                ensembleCommandService.update(ensemble, request)
        );
    }

    /** 합주 삭제 */
    public void deleteEnsemble(DeleteEnsembleRequest request) {
        // 합주 존재 유무 확인
        Ensemble ensemble = ensembleQueryService.getById(request.id());

        // timeslot 삭제
        timeslotCommandService.deleteAllByEnsembleId(ensemble.getId());

        // ensemble 삭제
        ensembleCommandService.delete(ensemble);
    }

    /** 세션 신청 */
    public EnsembleResponse applySession(NlwebUserDetails principal, ApplySessionRequest request) {
        // 합주 존재 유무 확인
        Ensemble ensemble = ensembleQueryService.getById(request.ensembleId());

        // 현재 세션 신청 가능한지 확인
        if (!programQueryService.isSessionApplyAvailable(ensemble.getProgram().getId())) {
            throw new ProgramApplyNotAvailableException(ensemble.getProgram().getId().toString());
        }

        // 이미 세션이 있는지 확인
        if (ensembleQueryService.isSessionExists(ensemble, request.sessionType())) {
            throw new DuplicateSessionException(request.sessionType());
        }

        String value = principal.getBatch() + "기 " + principal.getFullName();

        return EnsembleResponse.forPublic(
                ensembleCommandService.sessionApply(ensemble, request.sessionType(), value)
        );
    }

    public List<EnsembleResponse> getAllEnsemblesInProgram(GetAllEnsembleRequest request) {
        // 프로그램 존재 유무 확인
        Program program = programQueryService.getById(request.programId());

        return ensembleQueryService.getByProgramId(program.getId()).stream()
                .map(EnsembleResponse::forPublic)
                .toList();
    }
}
