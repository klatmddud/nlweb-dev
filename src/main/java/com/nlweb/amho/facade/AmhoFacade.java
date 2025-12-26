package com.nlweb.amho.facade;

import com.nlweb.amho.service.AmhoService;
import com.nlweb.amho.dto.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AmhoFacade {

    private final AmhoService amhoService;

    /** 현재 암호 조회 */
    public AmhoObject getActiveAmho() {
        return amhoService.getActiveAmho();
    }

    /** 새 암호 생성 - 기존 암호 비활성화 */
    @Transactional
    public AmhoObject resetAmho() {
        AmhoObject oldAmho = amhoService.getActiveAmho();

        if (oldAmho != null) {
            UUID oldAmhoId = oldAmho.getId();
            amhoService.deactivate(oldAmhoId);
        }

        return amhoService.create();
    }

}
