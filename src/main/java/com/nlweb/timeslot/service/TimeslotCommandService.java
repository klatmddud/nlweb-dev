package com.nlweb.timeslot.service;

import com.nlweb.user.entity.User;
import com.nlweb.timeslot.entity.Timeslot;
import com.nlweb.timeslot.repository.TimeslotRepository;
import com.nlweb.timeslot.dto.*;
import com.nlweb.ensemble.entity.Ensemble;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TimeslotCommandService {

    private final TimeslotRepository timeslotRepository;

    /** 새 시간표 생성 */
    public Timeslot create(User user, Ensemble ensemble, CreateTimeslotRequest request) {
        Timeslot timeslot = Timeslot.builder()
                .ensemble(ensemble)
                .user(user)
                .description(request.description())
                .startAt(request.startAt())
                .endAt(request.endAt())
                .build();

        Timeslot newTimeslot = timeslotRepository.save(timeslot);
        log.info("새 시간표 생성 완료: timeslotId={}", newTimeslot.getId());

        return newTimeslot;
    }

    /** 시간표 삭제 */
    public void delete(Timeslot timeslot) {
        timeslotRepository.delete(timeslot);
        log.info("시간표 삭제 완료: timeslotId={}", timeslot.getId());
    }

    public void deleteAllByEnsembleId(UUID ensembleId) {
        long deletedCount = timeslotRepository.deleteByEnsembleId(ensembleId);
        log.info("합주 연관 타임슬롯 삭제 완료: ensembleId={}, deletedCount={}", ensembleId, deletedCount);
    }

}
