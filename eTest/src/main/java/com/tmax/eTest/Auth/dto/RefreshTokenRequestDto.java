package com.tmax.eTest.Auth.dto;

import lombok.Getter;

@Getter
public class RefreshTokenRequestDto {
    private String accessToken;
    private String refreshToken;
}
