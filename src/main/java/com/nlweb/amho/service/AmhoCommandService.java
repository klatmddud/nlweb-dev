package com.nlweb.amho.service;

import com.nlweb.amho.exception.AmhoNotFoundException;
import com.nlweb.common.util.AmhoUtils;
import com.nlweb.amho.repository.AmhoRepository;
import com.nlweb.amho.entity.Amho;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AmhoCommandService {

    private final AmhoRepository amhoRepository;
    private final AmhoUtils amhoUtils;

    /** 새 암호 생성 */
    public Amho create() {
        Integer codeLength = amhoUtils.getAmhoCodeLength();
        String userCode;
        String adminCode;

        do {
            userCode = amhoUtils.generateAmhoCode(codeLength);
            adminCode = amhoUtils.generateAmhoCode(codeLength);

        } while(
                amhoRepository.findByUserCode(userCode).isPresent() ||
                amhoRepository.findByUserCode(adminCode).isPresent()
        );

        Amho amho = Amho.builder()
                .userCode(userCode)
                .adminCode(adminCode)
                .isActive(true)
                .build();

        Amho savedAmho = amhoRepository.save(amho);
        log.info("새 암호 생성: {}", savedAmho.getId());

        return savedAmho;
    }

    /** Id로 암호 비활성화 */
    @CacheEvict(value = {"amho", "active-amho"}, allEntries = true)
    public void deactivate(UUID id) {
        Amho amho = amhoRepository.findById(id)
                .orElseThrow(() -> new AmhoNotFoundException("id: " + id));

        amho.deactivate();
        Amho savedAmho = amhoRepository.save(amho);
        log.info("암호 비활성화 완료: {}", savedAmho.getId());
    }

}
