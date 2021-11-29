package com.tmax.eTest.Auth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ModifyUserInfoDto {
    private String nickname;

    private String email;

    private Gender gender;
}
