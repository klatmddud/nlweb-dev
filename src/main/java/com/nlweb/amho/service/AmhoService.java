package com.nlweb.amho.service;

import com.nlweb.amho.repository.AmhoRepository;
import com.nlweb.amho.entity.Amho;
import com.nlweb.amho.dto.AmhoObject;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.security.SecureRandom;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AmhoService {

    private final Integer codeLength = 8;
    private final AmhoRepository amhoRepository;

    /** 새 암호 생성 */
    @Transactional
    public AmhoObject create() {
        String userCode;
        String adminCode;

        // 새 코드 생성 (중복 X)
        do {
            userCode = generateCode(codeLength);
            adminCode = generateCode(codeLength);

        } while (
                amhoRepository.findByUserCode(userCode).isPresent() ||
                amhoRepository.findByAdminCode(adminCode).isPresent()
        );

        // 새 암호 저장
        Amho amho = Amho.builder()
                .userCode(userCode)
                .adminCode(adminCode)
                .isActive(true)
                .build();

        Amho savedAmho = amhoRepository.save(amho);
        log.info("새 암호 생성 완료: {}", savedAmho.getId());
        return AmhoObject.fromEntity(savedAmho);
    }

    /** ID로 암호 조회 */
    public AmhoObject getById(UUID id) {
        return amhoRepository.findById(id)
                .map(AmhoObject::fromEntity).orElse(null);
    }

    /** ID로 암호 비활성화 */
    @Transactional
    public void deactivate(UUID id) {
        Optional<Amho> optionalAmho = amhoRepository.findById(id);

        if (optionalAmho.isEmpty()) {
            return;
        }

        Amho amho = optionalAmho.get();
        amho.deactivate();
        amhoRepository.save(amho);
        log.info("암호 비활성화 완료: {}", amho.getId());
    }

    /** 현재 활성화된 암호 조회 */
    public AmhoObject getActiveAmho() {
        return amhoRepository.findByIsActiveTrue()
                .map(AmhoObject::fromEntity).orElse(null);
    }

    /** 랜덤 코드 생성 */
    private String generateCode(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }

        return code.toString();
    }
}
