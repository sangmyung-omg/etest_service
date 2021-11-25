package com.tmax.eTest.Auth.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginResponseDTO {
    String jwtToken;
    String email;
    String birthday;
    String nickname;
    String provider;
    String providerId;
    String refreshToken;
}
