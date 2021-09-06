package com.tmax.eTest.Admin.managementHistory.controller;

import com.tmax.eTest.Admin.managementHistory.model.ManagementHistory;
import com.tmax.eTest.Admin.managementHistory.model.RequestMapper;
import com.tmax.eTest.Admin.managementHistory.service.ManagementHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("history")
public class ManagementHistoryController {
    private final ManagementHistoryService managementHistoryService;

    @DeleteMapping()
    public ResponseEntity<Void> deleteManagementHistory(@RequestParam Long id){
        managementHistoryService.deleteManagementHistory(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public ResponseEntity<List<ManagementHistory>> getManagementRecordList(){
        return ResponseEntity.ok(managementHistoryService.getManagementHistoryList());
    }

    @PostMapping("request")
    public ResponseEntity<RequestMapper> createRequestMapper(@RequestBody RequestMapper requestMapper){
        return ResponseEntity.ok(managementHistoryService.createRequestMapper(requestMapper));
    }

    @DeleteMapping("request")
    public ResponseEntity<Void> deleteRequestMapper(@RequestParam Long id){
        managementHistoryService.deleteRequestMapper(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("request")
    public ResponseEntity<List<RequestMapper>> getRequestMapperList(){
        return ResponseEntity.ok(managementHistoryService.getRequestMapperList());
    }
}