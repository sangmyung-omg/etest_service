package com.tmax.eTest.Admin.managementHistory.controller;

import com.tmax.eTest.Admin.managementHistory.dto.DownloadServiceDTO;
import com.tmax.eTest.Admin.managementHistory.model.ManagementHistory;
import com.tmax.eTest.Admin.managementHistory.model.RequestMapper;
import com.tmax.eTest.Admin.managementHistory.service.ManagementHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("master/history")
public class ManagementHistoryController {
    private final ManagementHistoryService managementHistoryService;


    /**
     * 관리 이력 목록 조회
     */
    @GetMapping()
    public ResponseEntity<List<ManagementHistory>> getManagementRecordList(){
        return ResponseEntity.ok(managementHistoryService.getManagementHistoryList());
    }


    /**
     * 관리 이력 mapping 생성
     * @param requestMapper
     */
    @PostMapping("request_mapper")
    public ResponseEntity<RequestMapper> createRequestMapper(@RequestBody RequestMapper requestMapper){
        return ResponseEntity.ok(managementHistoryService.createRequestMapper(requestMapper));
    }

    /**
     * 관리 이력 mapping 삭제
     * @param id
     */
    @DeleteMapping("request_mapper")
    public ResponseEntity<Void> deleteRequestMapper(@RequestParam Long id){
        managementHistoryService.deleteRequestMapper(id);
        return ResponseEntity.ok().build();
    }


    /**
     * 관리 이력 mapping 목록 조회
     */
    @GetMapping("request_mapper")
    public ResponseEntity<List<RequestMapper>> getRequestMapperList(){
        return ResponseEntity.ok(managementHistoryService.getRequestMapperList());
    }


    /**
     * 다운로드 사유 팝업
     * @param downloadServiceDTO
     *      detail: 다운로드 받은 자료 (관리 이력 다운로드, 회원 관리 다운로드, ...)
     *      reason: 다운로드 사유
     */
    @PostMapping("download")
    public ResponseEntity<DownloadServiceDTO> downloadReasonPopup(@RequestBody DownloadServiceDTO downloadServiceDTO){
        return ResponseEntity.ok(managementHistoryService.downloadReasonPopup(downloadServiceDTO));
    }

//    @DeleteMapping()
//    public ResponseEntity<Void> deleteManagementHistory(@RequestParam Long id){
//        managementHistoryService.deleteManagementHistory(id);
//        return ResponseEntity.ok().build();
//    }
}