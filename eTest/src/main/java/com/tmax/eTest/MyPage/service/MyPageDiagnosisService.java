package com.tmax.eTest.MyPage.service;

import com.tmax.eTest.Common.model.report.DiagnosisReport;
import com.tmax.eTest.MyPage.repository.DiagnosisReportRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class MyPageDiagnosisService {

    @Qualifier("MY-DiagnosisReportRepo")
    @Autowired
    DiagnosisReportRepo diagnosisReportRepo;

    public List<DiagnosisReport> getDiagnosisList(String UserUuid) {
       List<DiagnosisReport> diagnosisReport_list = diagnosisReportRepo.findAllByUserUuid(UserUuid);
        return diagnosisReport_list;
    }


}
