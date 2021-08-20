package com.tmax.eTest.MyPage.controller;

import com.tmax.eTest.Auth.dto.CMRespDto;
import com.tmax.eTest.Auth.dto.PrincipalDetails;
import com.tmax.eTest.Common.model.report.MinitestReport;
import com.tmax.eTest.MyPage.dto.MiniTestReportHistoryDTO;
import com.tmax.eTest.MyPage.service.MyPageMiniTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class MyPageMiniTestController {
    @Autowired
    MyPageMiniTestService myPageMiniTestService;

    @GetMapping("/myPage/getMiniTestList")
    public CMRespDto<?> getMiniTestList(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<MiniTestReportHistoryDTO> minitestReports = myPageMiniTestService.getMinitestReportList(principalDetails.getUserUuid());
        return new CMRespDto<>(200,"test",minitestReports);
    }
}
