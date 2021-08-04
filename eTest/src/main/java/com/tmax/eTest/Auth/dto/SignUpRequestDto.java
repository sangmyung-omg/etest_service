package com.tmax.eTest.Auth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Data
public class SignUpRequestDto {

    private String providerId;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String email;

    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    private Role role;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String password;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private Boolean event_sms_agreement;

    private Boolean account_active;

}