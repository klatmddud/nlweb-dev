package com.nlweb.amho.facade;

import com.nlweb.amho.dto.AmhoResponse;
import com.nlweb.amho.entity.Amho;
import com.nlweb.amho.service.*;
import com.nlweb.amho.log.AmhoLogger;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AmhoFacade {

    private final AmhoCommandService amhoCommandService;
    private final AmhoQueryService amhoQueryService;

    /** 현재 암호 조회 */
    public AmhoResponse getActiveAmho(UUID userId, String username) {
        try {
            // Amho get attempt will be logged on success/failure
            
            Amho amho = amhoQueryService.getActiveAmho();

            AmhoLogger.logSuccessAmhoEvent("get_active_amho", userId, username,
                    Map.of("amdho_id", amho.getId().toString(), "created_at", amho.getCreatedAt().toString()));
            
            return AmhoResponse.forGet(amho);
            
        } catch (Exception e) {
            AmhoLogger.logFailureAmhoEvent("get_active_amho", userId, username, e, null);
            throw e;
        }
    }

    /** 새 암호 생성 - 기존 암호 비활성화 */
    @Transactional
    public AmhoResponse resetAmho(UUID userId, String username) {
        try {
            // Amho reset attempt will be logged on success/failure
            
            Amho oldAmho = amhoQueryService.getActiveAmho();

            amhoCommandService.deactivate(oldAmho.getId());

            Amho newAmho = amhoCommandService.create();

            AmhoLogger.logSuccessAmhoEvent("reset_amho", userId, username,
                    Map.of( "old_amho_id", oldAmho.getId().toString(),
                            "new_amho_id", newAmho.getId().toString()));
            
            return AmhoResponse.forReset(newAmho);
            
        } catch (Exception e) {
            AmhoLogger.logFailureAmhoEvent("reset_amho", userId, username, e, null);
            throw e;
        }
    }

}
