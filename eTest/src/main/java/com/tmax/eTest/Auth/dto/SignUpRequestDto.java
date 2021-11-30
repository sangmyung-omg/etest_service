package com.tmax.eTest.Auth.dto;

import com.tmax.eTest.Auth.authenum.AuthProvider;
import com.tmax.eTest.Auth.authenum.Role;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class SignUpRequestDto {

    private String providerId;

    private String email;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private Boolean event_sms_agreement;

    private Boolean account_active;

}