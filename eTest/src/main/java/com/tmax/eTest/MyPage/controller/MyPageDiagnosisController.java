package com.tmax.eTest.MyPage.controller;

import com.tmax.eTest.Auth.dto.CMRespDto;
import com.tmax.eTest.Auth.model.PrincipalDetails;
import com.tmax.eTest.Common.model.report.DiagnosisReport;
import com.tmax.eTest.MyPage.dto.DiagnosisReportHistoryDTO;
import com.tmax.eTest.MyPage.service.MyPageDiagnosisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/user")
public class MyPageDiagnosisController {

    @Autowired
    MyPageDiagnosisService diagnosisService;

    @GetMapping("/myPage/getDiagnosisList")
    public CMRespDto<?> getDiagnosisList(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<DiagnosisReport> diagnosisReport = diagnosisService.getDiagnosisList(principalDetails.getUserUuid());
        List<DiagnosisReportHistoryDTO> diagnosisReportHistoryDTOList = DiagnosisReportHistoryDTO.toDtoList(diagnosisReport);
        return new CMRespDto<>(200, "test", diagnosisReportHistoryDTOList);
    }
}
