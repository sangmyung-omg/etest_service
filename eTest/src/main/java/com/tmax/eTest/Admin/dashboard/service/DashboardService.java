package com.tmax.eTest.Admin.dashboard.service;

import com.tmax.eTest.Admin.dashboard.dto.FilterQueryDTO;
import com.tmax.eTest.Admin.dashboard.dto.FilterDTO;
import com.tmax.eTest.Admin.dashboard.repository.DiagnosisReportRepository;
import com.tmax.eTest.Admin.dashboard.repository.MinitestReportRepository;
import com.tmax.eTest.Admin.dashboard.repository.StatementRepository;
import com.tmax.eTest.Auth.dto.Gender;
import com.tmax.eTest.Common.model.report.DiagnosisReport;
import com.tmax.eTest.Common.model.report.MinitestReport;
import com.tmax.eTest.LRS.dto.GetStatementInfoDTO;
import com.tmax.eTest.LRS.model.Statement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    @Autowired
    @Qualifier(value ="AD-StatementRepository")
    private final StatementRepository statementRepository;
    @Autowired
    @Qualifier(value = "AD-DiagnosisReportRepository")
    private final DiagnosisReportRepository diagnosisReportRepository;
    private final MinitestReportRepository minitestReportRepository;

    public FilterQueryDTO filterQueryBuilder(FilterDTO filterDTO) {
        FilterQueryDTO filterQueryDTO = FilterQueryDTO.builder()
                .gender(filterDTO.getGender())
                .investmentExperience(filterDTO.getInvestmentExperience())
                .build();
        if (filterDTO.getDateFrom() != null & filterDTO.getDateTo() != null){
            filterQueryDTO.setDateFrom(Timestamp.valueOf(filterDTO.getDateFrom()));
            filterQueryDTO.setDateTo(Timestamp.valueOf(filterDTO.getDateTo().plusDays(1)));
        }
        if (filterDTO.getAgeGroup() != null){
            LocalDate currentDateTime = LocalDate.now();
            filterQueryDTO.setAgeGroupLowerBound(currentDateTime.minusYears(filterDTO.getAgeGroup() + 10));
            filterQueryDTO.setAgeGroupUpperBound(currentDateTime.minusYears(filterDTO.getAgeGroup()));
        }
        return filterQueryDTO;
    }

    public List<DiagnosisReport> getDiagnosis(FilterDTO filterDTO) {
        return diagnosisReportRepository.filter(filterQueryBuilder(filterDTO));
    }

    public List<MinitestReport> getMinitest(FilterDTO filterDTO) {
        return minitestReportRepository.filter(filterQueryBuilder(filterDTO));
    }

    public List<Statement> getStatement(FilterDTO filterDTO) {
        return statementRepository.filter(filterQueryBuilder(filterDTO));
    }
//    public Long getPlaytime(FilterDTO filterDTO) {
//        GetStatementInfoDTO getStatementInfoDTO = new GetStatementInfoDTO();
//        String dateFromStr = filterDTO.getDateFrom().toString().substring(0,10);
//        String dateToStr = filterDTO.getDateTo().plusDays(1).toString().substring(0,10);
//        return statementRepository.findAllByFilter( dateFromStr, dateToStr,Gender.female.toString());
//    }
}


