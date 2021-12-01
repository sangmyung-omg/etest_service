package com.tmax.eTest.MyPage.service;

import com.tmax.eTest.Common.model.report.MinitestReport;
import com.tmax.eTest.MyPage.repository.MinitestReportRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MyPageMiniTestService {

    @Qualifier("MY-MinitestReportRepo")
    @Autowired
    MinitestReportRepo minitestReportRepo;

    @Transactional(readOnly = true)
    public List<MinitestReport> getMinitestReportList(String UserUuid) {

        List<MinitestReport> minitestReports = minitestReportRepo.findAllByUserUuid(UserUuid);




        return minitestReports;
    }
}
