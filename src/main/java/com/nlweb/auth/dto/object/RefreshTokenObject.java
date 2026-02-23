package com.nlweb.auth.dto.object;

import lombok.*;

@Data
@Builder
public class RefreshTokenObject {

    private String refreshToken;
    private Long refreshExpiresIn;

}
