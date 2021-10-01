package com.tmax.eTest.Admin.permissionManagement.dto;

import com.tmax.eTest.Auth.dto.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionUserMasterSearchDTO {
    private String userUuid;
    private Role role;
    private String name;
}
