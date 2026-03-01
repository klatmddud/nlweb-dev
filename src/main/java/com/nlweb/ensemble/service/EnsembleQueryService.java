package com.nlweb.ensemble.service;

import com.nlweb.ensemble.entity.Ensemble;
import com.nlweb.ensemble.exception.EnsembleNotFoundException;
import com.nlweb.ensemble.repository.EnsembleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Locale;
import java.util.UUID;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EnsembleQueryService {

    private final EnsembleRepository ensembleRepository;

    /** 합주 ID로 찾기 */
    public Ensemble getById(UUID id) {
        return ensembleRepository.findById(id).orElseThrow(() -> new EnsembleNotFoundException(id.toString()));
    }

    /** 프로그램에 등록된 합주 전체 조회 */
    public List<Ensemble> getByProgramId(UUID id) {
        return ensembleRepository.findByProgramId(id);
    }

    /** artist와 title이 이미 존재하는지 확인 */
    public boolean isArtistAndTitleExists(String artist, String title) {
        return ensembleRepository.existsByArtistAndTitle(artist, title);
    }

    /** dayOfWeek, startTime, endTime이 이미 존재하는지 확인 */
    public boolean isScheduleExists(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        if (dayOfWeek == null || startTime == null || endTime == null) {
            return false;
        }

        return ensembleRepository.existsByDayOfWeekAndStartTimeAndEndTime(dayOfWeek, startTime, endTime);
    }

    /** 특정 세션(vocal, lead_guitar 등)이 이미 존재하는지 확인 */
    public boolean isSessionExists(Ensemble ensemble, String sessionName) {
        if (ensemble == null || !StringUtils.hasText(sessionName)) {
            return false;
        }

        return switch (normalizeSessionName(sessionName)) {
            case "vocal" -> StringUtils.hasText(ensemble.getVocal());
            case "lead_guitar" -> StringUtils.hasText(ensemble.getLeadGuitar());
            case "rhythm_guitar" -> StringUtils.hasText(ensemble.getRhythmGuitar());
            case "bass" -> StringUtils.hasText(ensemble.getBass());
            case "drum" -> StringUtils.hasText(ensemble.getDrum());
            case "piano" -> StringUtils.hasText(ensemble.getPiano());
            case "synth" -> StringUtils.hasText(ensemble.getSynth());
            case "etc" -> StringUtils.hasText(ensemble.getEtc());
            default -> throw new IllegalArgumentException("지원하지 않는 sessionName입니다: " + sessionName);
        };
    }

    private String normalizeSessionName(String sessionName) {
        return sessionName.trim()
                .toLowerCase(Locale.ROOT)
                .replace("-", "_")
                .replace(" ", "_");
    }

}
