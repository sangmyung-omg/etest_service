package com.tmax.eTest.Admin.dashboard.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tmax.eTest.Admin.dashboard.dto.FilterQueryDTO;
import com.tmax.eTest.LRS.model.Statement;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.sql.Timestamp;

import java.util.List;

import static com.tmax.eTest.Common.model.report.QDiagnosisReport.diagnosisReport;
import static com.tmax.eTest.Common.model.report.QMinitestReport.minitestReport;
import static com.tmax.eTest.Common.model.user.QUserMaster.userMaster;
import static com.tmax.eTest.LRS.model.QStatement.statement;

@Repository
public class UserCreateTimeRepository extends UserFilterRepository {
    private final JPAQueryFactory query;

    public UserCreateTimeRepository(EntityManager entityManager) {
        this.query = new JPAQueryFactory(entityManager);
    }

    public List<Statement> filter(FilterQueryDTO filterQueryDTO) {
        return query.select(statement)
                .from(statement)
                .join(userMaster).on(userMaster.userUuid.eq(statement.userId))
                .where(
                        dateFilter(filterQueryDTO.getDateFrom(), filterQueryDTO.getDateTo()),
                        genderFilter(filterQueryDTO.getGender()),
                        ageGroupFilter(filterQueryDTO.getAgeGroupLowerBound(), filterQueryDTO.getAgeGroupUpperBound()),
                        statement.actionType.eq("register"),
                        statement.sourceType.eq("application")
                )
                .fetch();
    }
    private BooleanExpression dateFilter(Timestamp dateFrom, Timestamp dateTo){
        if (dateFrom == null & dateTo == null)
            return null;
        return statement.statementDate.between(dateFrom, dateTo);
    }
}
