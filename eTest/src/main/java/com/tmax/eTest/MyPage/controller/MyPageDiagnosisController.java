package com.tmax.eTest.MyPage.controller;

import com.tmax.eTest.Auth.dto.CMRespDto;
import com.tmax.eTest.Auth.dto.PrincipalDetails;
import com.tmax.eTest.Common.model.report.DiagnosisReport;
import com.tmax.eTest.MyPage.dto.DeleteDiagnosisReportDTO;
import com.tmax.eTest.MyPage.service.MyPageDiagnosisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class MyPageDiagnosisController {
    @Autowired
    MyPageDiagnosisService diagnosisService;

    @GetMapping("/myPage/getDiagnosisList")
    public CMRespDto<?> getDiagnosisList(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<DiagnosisReport> diagnosisReport = diagnosisService.getDiagnosisList(principalDetails.getUserUuid());
//        Optional<DiagnosisReport> diagnosisReport = diagnosisService.getDiagnosisList("022ed3de-08b3-4874-95b7-0a557581d85b");
        return new CMRespDto<>(200, "test", diagnosisReport);
    }

    @PostMapping("/myPage/deleteDiagnosisList")
    public CMRespDto<?> deleteDiagnosisList(@AuthenticationPrincipal PrincipalDetails principalDetails, DeleteDiagnosisReportDTO deleteDiagnosisReportDTO) {
        return null;
    }



}
