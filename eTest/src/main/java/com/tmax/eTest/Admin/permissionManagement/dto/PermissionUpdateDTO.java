package com.tmax.eTest.Admin.permissionManagement.dto;

import com.tmax.eTest.Auth.dto.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PermissionUpdateDTO {
    List<String> userUuidList;
    Role role;
    String ip;
}
