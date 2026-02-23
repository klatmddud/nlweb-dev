package com.nlweb.auth.dto.object;

import lombok.*;

@Data
@Builder
public class AccessTokenObject {

    private String accessToken;
    private Long accessExpiresIn;

}
