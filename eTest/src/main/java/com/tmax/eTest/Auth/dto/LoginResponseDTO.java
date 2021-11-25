package com.tmax.eTest.Auth.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginResponseDTO {
    private String jwtToken;
    private String email;
    private String birthday;
    private String nickname;
    private String provider;
    private String providerId;
    private String refreshToken;
}
