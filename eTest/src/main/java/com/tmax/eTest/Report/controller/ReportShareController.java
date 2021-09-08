package com.tmax.eTest.Report.controller;

import javax.servlet.http.HttpServletRequest;

import com.tmax.eTest.Report.dto.ReportShareCreateDTO;
import com.tmax.eTest.Report.dto.ReportShareKeyDTO;
import com.tmax.eTest.Report.service.ReportShareService;
import com.tmax.eTest.Report.util.UserIdFetchTool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.security.access.prepost.PreAuthorize;


@CrossOrigin("*")
@RestController
public class ReportShareController {
    @Autowired
    private ReportShareService reportShareService;
    
    @Autowired
    private UserIdFetchTool userIdFetchTool;
    
    @GetMapping(value="/report/mini/share")
    public ResponseEntity<?> getMethodName(@RequestParam(value = "sharekey") String key) {
        Object output = reportShareService.getReportDataFromKey(key);
        return ResponseEntity.ok().body(output);
    }

    @PostMapping(value="/report/mini/share")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> createShareReport(HttpServletRequest request, @RequestBody ReportShareCreateDTO createDTO){
        String userId = userIdFetchTool.getID(request);
        String key = reportShareService.createReportShareKey(userId, createDTO);
        return ResponseEntity.ok().body(ReportShareKeyDTO.builder().sharekey(key).build());
    }
}
