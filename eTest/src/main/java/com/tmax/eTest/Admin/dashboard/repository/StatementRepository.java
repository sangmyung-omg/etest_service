package com.tmax.eTest.Admin.dashboard.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.tmax.eTest.LRS.model.QStatement.statement;
import static com.tmax.eTest.Common.model.user.QUserMaster.userMaster;

import com.tmax.eTest.Admin.dashboard.dto.FilterQueryDTO;
import com.tmax.eTest.LRS.model.Statement;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository("AD-StatementRepository")
public class StatementRepository extends UserFilterRepository {
    private final JPAQueryFactory query;
    LocalDate currentDate = LocalDate.now();
    SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd");
    Date time = new Date();
    String time1 = format1.format(time);
    String time2 = time1.substring(10);

    public StatementRepository(EntityManager entityManager) {
        this.query = new JPAQueryFactory(entityManager);
    }

    public List<Statement> filter(FilterQueryDTO filterQueryDTO) {
        return query.select(statement)
                .from(statement)
                .join(userMaster)
                .on(statement.userId.eq(userMaster.userUuid))
                .where(
                        dateFilter(filterQueryDTO.getDateFrom(), filterQueryDTO.getDateTo()),
                        genderFilter(filterQueryDTO.getGender()),
                        ageGroupFilter(filterQueryDTO.getAgeGroupLowerBound(), filterQueryDTO.getAgeGroupUpperBound())
                )
                .fetch();
    }
    private BooleanExpression dateFilter(Timestamp dateFrom, Timestamp dateTo){
        if (dateFrom == null & dateTo == null)
            return null;
        return statement.statementDate.between(dateFrom, dateTo);
    }
}


