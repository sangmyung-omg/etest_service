package com.tmax.eTest.Admin.permissionManagement.controller;

import com.tmax.eTest.Admin.permissionManagement.dto.PermissionManagementDTO;
import com.tmax.eTest.Admin.permissionManagement.dto.PermissionUpdateDTO;
import com.tmax.eTest.Admin.permissionManagement.dto.PermissionUserMasterSearchDTO;
import com.tmax.eTest.Admin.permissionManagement.service.PermissionManagementService;
import com.tmax.eTest.Auth.dto.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("master/permission_management")
public class PermissionManagementController {
    private final PermissionManagementService permissionManagementService;

    /**
     * 관리자 조회
     * @param role      권한 필터 (MASTER, SUB_MASTER)
     * @param search    이름, 이메일 검색 조건
     */
    @GetMapping()
    public ResponseEntity<List<PermissionManagementDTO>> searchMaster
            (@RequestParam(required = false) String role, @RequestParam(required = false) String search){
        if (role == null)
            return ResponseEntity.ok(permissionManagementService.searchMaster(null, search));
        return ResponseEntity.ok(permissionManagementService.searchMaster(Role.valueOf(role), search));
    }

    /**
     * 관리자 생성에서 이름 검색
     * @param search    이름 검색 조건
     */
    @GetMapping("user_search")
    public ResponseEntity<List<PermissionUserMasterSearchDTO>> addMasterSearch(@RequestParam(required = false) String search){
        return ResponseEntity.ok(permissionManagementService.addMasterSearch(search));
    }

    /**
     * 관리자 권한 여러개 삭제 / 관리자 권한 변경
     * @param permissionUpdateDTO   권한을 변경할 관리자 리스트, 변경할 권한, 변경할 IP
     */
    @PostMapping()
    public void updatePermission(@RequestBody PermissionUpdateDTO permissionUpdateDTO){
        permissionManagementService.updatePermission(permissionUpdateDTO);
    }
}
