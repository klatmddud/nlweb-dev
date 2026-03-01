package com.nlweb.timeslot.service;

import com.nlweb.timeslot.entity.Timeslot;
import com.nlweb.timeslot.repository.TimeslotRepository;
import com.nlweb.timeslot.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TimeslotQueryService {

    private final TimeslotRepository timeslotRepository;

    /** 시간표 ID로 찾기 */
    public Timeslot getById(UUID id) {
        return timeslotRepository.findById(id)
                .orElseThrow(() -> new TimeslotNotFoundException(id.toString()));
    }

    /** 시간대가 겹치는 시간표가 있는지 확인 */
    public void validateTimeOverlap(LocalDateTime startAt, LocalDateTime endAt) {
        if (timeslotRepository.existsByStartAtLessThanAndEndAtGreaterThan(endAt, startAt)){
            throw new TimeslotOverlapException("겹치는 시간대가 이미 존재합니다.");
        }
    }

}
