package com.tmax.eTest.MyPage.controller;

import com.tmax.eTest.Auth.dto.CMRespDto;
import com.tmax.eTest.Auth.dto.PrincipalDetails;
import com.tmax.eTest.Common.model.report.DiagnosisReport;
import com.tmax.eTest.Common.model.report.MinitestReport;
import com.tmax.eTest.MyPage.service.MyPageDiagnosisService;
import com.tmax.eTest.MyPage.service.MyPageMiniTestService;
import com.tmax.eTest.Report.service.MiniTestReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class MyPageMiniTestController {
    @Autowired
    MyPageMiniTestService myPageMiniTestService;
    @Autowired
    MiniTestReportService miniTestReportService;

    @GetMapping("/myPage/getMiniTestList")
    public CMRespDto<?> getMiniTestList(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<MinitestReport> minitestReports = myPageMiniTestService.getMinitestReportList(principalDetails.getUserUuid());
        return new CMRespDto<>(200,"test",minitestReports);
    }
}
