package com.nlweb.core.properties;

import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;

@Data
@Component
@Validated
@ConfigurationProperties(prefix = "amho")
public class AmhoProperties {

    @NotNull(message = "Amho Code Length는 필수입니다")
    @Min(value = 4, message = "Amho Code Length는 최소 4이어야 합니다.")
    private Integer codeLength;

}
