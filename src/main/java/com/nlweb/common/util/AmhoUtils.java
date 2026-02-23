package com.nlweb.common.util;

import com.nlweb.core.properties.AmhoProperties;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.security.SecureRandom;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmhoUtils {

    private final AmhoProperties amhoProperties;

    /** 암호 길이 반환 */
    public Integer getAmhoCodeLength() {
        return amhoProperties.getCodeLength();
    }

    /** 랜덤 암호 생성 */
    public String generateAmhoCode(Integer length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }

        return code.toString();
    }

}
