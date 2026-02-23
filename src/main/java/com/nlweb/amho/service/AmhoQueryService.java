package com.nlweb.amho.service;

import com.nlweb.amho.repository.AmhoRepository;
import com.nlweb.amho.entity.Amho;
import com.nlweb.amho.exception.AmhoNotFoundException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AmhoQueryService {

    private final AmhoRepository amhoRepository;

    /** Id로 암호 조회 */
    @Cacheable(value = "amho", key = "#id")
    public Amho getById(UUID id) {
        return amhoRepository.findById(id).orElseThrow(() -> new AmhoNotFoundException("id: " + id));
    }

    /** 코드로 암호 조회 */
    @Cacheable(value = "amho", key = "#code")
    public Amho getByCode(String code) {
        Amho amho = amhoRepository.findByUserCode(code).orElse(null);

        if (amho == null) {
            amho = amhoRepository.findByAdminCode(code)
                    .orElseThrow(() -> new AmhoNotFoundException("code: " + code));
        }

        return amho;
    }

    /** 활성화된 암호 조회 */
    @Cacheable(value = "active-amho")
    public Amho getActiveAmho() {
        return amhoRepository.findByIsActiveTrue()
                .orElseThrow(() -> new AmhoNotFoundException("active_amho"));
    }

}
