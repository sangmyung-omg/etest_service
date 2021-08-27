package com.tmax.eTest.ManageUser.model.dto;

import com.tmax.eTest.Auth.dto.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPopupDTO {
    String user_uuid;
    String nick_name;
    Gender gender;
    String email;
    LocalDate birthday;
}
