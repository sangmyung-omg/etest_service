package com.tmax.eTest.ManageUser.model.dto;

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
    String email;
    LocalDate birthday;
}
