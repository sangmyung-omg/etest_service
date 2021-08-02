package com.tmax.eTest.Auth.dto;

import com.tmax.eTest.Auth.model.AuthProvider;
import com.tmax.eTest.Auth.model.Role;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class SignUpRequestDto {
    private String name;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String email;
    private LocalDate birthday;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String password;
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;
}