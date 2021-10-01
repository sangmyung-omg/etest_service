package com.tmax.eTest.Auth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ModifyUserInfoDto {
    private String nickname;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    private String email;

    private Gender gender;
}
