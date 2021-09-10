package com.tmax.eTest.Report.controller;

import javax.servlet.http.HttpServletRequest;

import com.tmax.eTest.Report.dto.ReportShareCreateDTO;
import com.tmax.eTest.Report.dto.ReportShareKeyDTO;
import com.tmax.eTest.Report.service.DiagnosisDetailRecordService;
import com.tmax.eTest.Report.service.ReportShareService;
import com.tmax.eTest.Report.util.UserIdFetchTool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.security.access.prepost.PreAuthorize;

import com.tmax.eTest.Contents.controller.answer.AnswerControllerV1;

@CrossOrigin("*")
@RestController
public class ReportShareController {
    @Autowired
    private ReportShareService reportShareService;
    
    @Autowired
    private UserIdFetchTool userIdFetchTool;

    @Autowired
    private DiagnosisDetailRecordService diagnosisDetailRecordService;

    @Autowired
    private AnswerControllerV1 answerControllerV1;
    
    @GetMapping(value="/report/mini/share")
    public ResponseEntity<?> getMethodNameMinitest(@RequestParam(value = "sharekey") String key) {
        Object output = reportShareService.getReportDataFromKey(key);

        if(output == null)
            return new ResponseEntity<>("false", HttpStatus.NOT_FOUND);

        return ResponseEntity.ok().body(output);
    }

    @GetMapping(value="/report/diag/share")
    public ResponseEntity<?> getMethodNameDiag(@RequestParam(value = "sharekey") String key) {
        Object output = reportShareService.getReportDataFromKey(key);

        if(output == null)
            return new ResponseEntity<>("false", HttpStatus.NOT_FOUND);

        return ResponseEntity.ok().body(output);
    }

    @GetMapping(value="/report/diag/share/bypart/{part}")
    public ResponseEntity<?> getMethodNameDiagParts(@RequestParam(value = "sharekey") String key, @PathVariable("part") String partName) {
        ReportShareCreateDTO keydata = reportShareService.getDataFromKey(key);
        if(keydata == null)
            return ResponseEntity.internalServerError().body("Invalid sharekey");

        Object output = null;
        try {
            output = diagnosisDetailRecordService.getDiagnosisRecordDetail(keydata.getUserId(), keydata.getProbSetId(), partName);
        }
        catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Cannot get part record detail. " + partName);
        }

        if(output == null)
            return new ResponseEntity<>("false", HttpStatus.NOT_FOUND);

        return ResponseEntity.ok().body(output);
    }

    @GetMapping(value="/report/diag/share/solution")
    public ResponseEntity<?> getMethodNameDiagSolution(@RequestParam(value = "sharekey") String key) {
        ReportShareCreateDTO keydata = reportShareService.getDataFromKey(key);
        if(keydata == null)
            return ResponseEntity.internalServerError().body("Invalid sharekey");

        Object output;
        try {
            output = answerControllerV1.problem(keydata.getProbSetId());
        }
        catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Cannot get solution");
        }

        if(output == null)
            return new ResponseEntity<>("false", HttpStatus.NOT_FOUND);

        return ResponseEntity.ok().body(output);
    }

    @GetMapping(value="/report/share")
    public ResponseEntity<?> getMethodName(@RequestParam(value = "sharekey") String key) {
        Object output = reportShareService.getReportDataFromKey(key);

        if(output == null)
            return new ResponseEntity<>("false", HttpStatus.NOT_FOUND);

        return ResponseEntity.ok().body(output);
    }

    @PostMapping(value="/report/mini/share")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> createShareReportMinitest(HttpServletRequest request, @RequestBody ReportShareCreateDTO createDTO){
        String userId = userIdFetchTool.getID(request);
        String key = reportShareService.createReportShareKey(ReportShareService.TYPE_MINITEST, userId, createDTO.getProbSetId(), createDTO.getExpire());
        return ResponseEntity.ok().body(ReportShareKeyDTO.builder().sharekey(key).build());
    }

    @PostMapping(value="/report/diag/share")
    // @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> createShareReportDiagnosis(HttpServletRequest request, @RequestBody ReportShareCreateDTO createDTO){
        String userId = userIdFetchTool.getID(request);
        String key = reportShareService.createReportShareKey(ReportShareService.TYPE_DIAG, userId, createDTO.getProbSetId(), createDTO.getExpire());
        return ResponseEntity.ok().body(ReportShareKeyDTO.builder().sharekey(key).build());
    }

    @PostMapping(value="/report/share")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> createShareReport(HttpServletRequest request, @RequestBody ReportShareCreateDTO createDTO){
        String userId = userIdFetchTool.getID(request);
        String key = reportShareService.createReportShareKey(userId, createDTO);
        return ResponseEntity.ok().body(ReportShareKeyDTO.builder().sharekey(key).build());
    }
}
