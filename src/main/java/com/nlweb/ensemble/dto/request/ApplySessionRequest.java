package com.nlweb.ensemble.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record ApplySessionRequest(
        UUID ensembleId,
        @NotBlank(message = "세션 타입은 필수입니다.")
        @Pattern(
                regexp = "^(vocal|lead_guitar|rhythm_guitar|bass|drum|piano|synth|etc)$",
                message = "sessionType은 vocal, lead_guitar, rhythm_guitar, bass, drum, piano, synth, etc 중 하나여야 합니다."
        )
        String sessionType
) { }
