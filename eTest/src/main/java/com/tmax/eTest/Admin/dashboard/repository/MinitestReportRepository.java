package com.tmax.eTest.Admin.dashboard.repository;

import static com.tmax.eTest.Common.model.report.QDiagnosisReport.diagnosisReport;
import static com.tmax.eTest.Common.model.report.QMinitestReport.minitestReport;
import static com.tmax.eTest.Common.model.user.QUserMaster.userMaster;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tmax.eTest.Admin.dashboard.dto.FilterQueryDTO;
import com.tmax.eTest.Common.model.report.MinitestReport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class MinitestReportRepository extends UserFilterRepository {
    private final JPAQueryFactory query;

    public MinitestReportRepository (EntityManager entityManager) {
        this.query = new JPAQueryFactory(entityManager);
    }

    public List<MinitestReport> filter(FilterQueryDTO filterQueryDTO) {
        return query.select(minitestReport)
                .from(minitestReport)
                .join(userMaster).on(userMaster.userUuid.eq(minitestReport.userUuid))
                .where(
                        investmentExperienceFilter(filterQueryDTO.getInvestmentExperience()),
                        dateFilter(filterQueryDTO.getDateFrom(), filterQueryDTO.getDateTo()),
                        genderFilter(filterQueryDTO.getGender()),
                        ageGroupFilter(filterQueryDTO.getAgeGroupLowerBound(), filterQueryDTO.getAgeGroupUpperBound())
                )
                .fetch();
    }

    private BooleanExpression dateFilter(Timestamp dateFrom, Timestamp dateTo){
        if (dateFrom == null & dateTo == null)
            return null;
        return minitestReport.minitestDate.between(dateFrom, dateTo);
    }
}
