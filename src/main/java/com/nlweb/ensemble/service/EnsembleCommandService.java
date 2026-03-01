package com.nlweb.ensemble.service;

import com.nlweb.program.entity.Program;
import com.nlweb.ensemble.entity.Ensemble;
import com.nlweb.ensemble.repository.EnsembleRepository;
import com.nlweb.ensemble.dto.request.CreateEnsembleRequest;
import com.nlweb.ensemble.dto.request.UpdateEnsembleRequest;
import com.nlweb.ensemble.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EnsembleCommandService {

    private final EnsembleRepository ensembleRepository;

    /** 새 합주 생성 */
    public Ensemble create(Program program, CreateEnsembleRequest request) {
        Ensemble ensemble = Ensemble.builder()
                .program(program)
                .artist(request.artist())
                .title(request.title())
                .vocal(request.vocal())
                .leadGuitar(request.leadGuitar())
                .rhythmGuitar(request.rhythmGuitar())
                .bass(request.bass())
                .drum(request.drum())
                .piano(request.piano())
                .synth(request.synth())
                .etc(request.etc())
                .build();

        Ensemble savedEnsemble = ensembleRepository.save(ensemble);
        log.info("새 합주 생성: {}", savedEnsemble.getId());

        return savedEnsemble;
    }

    /** 합주 정보 업데이트 */
    public Ensemble update(Ensemble ensemble, UpdateEnsembleRequest request) {

        ensemble.setArtist(request.artist() == null? ensemble.getArtist() : request.artist());
        ensemble.setTitle(request.title() == null? ensemble.getTitle() : request.title());
        ensemble.setVocal(request.vocal() == null? ensemble.getVocal() : request.vocal());
        ensemble.setLeadGuitar(request.leadGuitar() == null? ensemble.getLeadGuitar() : request.leadGuitar());
        ensemble.setRhythmGuitar(request.rhythmGuitar() == null? ensemble.getRhythmGuitar() : request.rhythmGuitar());
        ensemble.setBass(request.bass() == null? ensemble.getBass() : request.bass());
        ensemble.setDrum(request.drum() == null? ensemble.getDrum() : request.drum());
        ensemble.setPiano(request.piano() == null? ensemble.getPiano() : request.piano());
        ensemble.setSynth(request.synth() == null? ensemble.getSynth() : request.synth());
        ensemble.setEtc(request.etc() == null? ensemble.getEtc() : request.etc());

        Ensemble newEnsemble = ensembleRepository.save(ensemble);
        log.info("합주 정보가 수정되었습니다: {}", ensemble.getId());

        return newEnsemble;

    }

    /** 합주 삭제 */
    public void delete(Ensemble ensemble) {
        ensembleRepository.delete(ensemble);
        log.info("합주 삭제 완료: {}", ensemble.getId());
    }

    /** 세션 신청 */
    public Ensemble sessionApply(Ensemble ensemble, String sessionType, String value) {
        String normalizedSessionType = sessionType.trim()
                .toLowerCase()
                .replace("-", "_")
                .replace(" ", "_");

        switch (normalizedSessionType) {
            case "vocal" -> ensemble.setVocal(value);
            case "lead_guitar" -> ensemble.setLeadGuitar(value);
            case "rhythm_guitar" -> ensemble.setRhythmGuitar(value);
            case "bass" -> ensemble.setBass(value);
            case "drum" -> ensemble.setDrum(value);
            case "piano" -> ensemble.setPiano(value);
            case "synth" -> ensemble.setSynth(value);
            case "etc" -> ensemble.setEtc(value);
            default -> throw new IllegalArgumentException("지원하지 않는 sessionType입니다: " + sessionType);
        }

        Ensemble updatedEnsemble = ensembleRepository.save(ensemble);
        log.info("합주 세션 신청 완료: ensembleId={}, sessionType={}", ensemble.getId(), normalizedSessionType);
        return updatedEnsemble;
    }
}
