package com.tmax.eTest.Admin.permissionManagement.dto;

import com.tmax.eTest.Auth.dto.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionManagementDTO {
    private String userUuid;
    private Role role;
    private String name;
    private String email;
    private String ip;
    private LocalDateTime createDate;
}
