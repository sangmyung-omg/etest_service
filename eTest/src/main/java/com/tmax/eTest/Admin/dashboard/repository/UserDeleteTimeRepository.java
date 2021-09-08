package com.tmax.eTest.Admin.dashboard.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tmax.eTest.Admin.dashboard.dto.FilterQueryDTO;
import com.tmax.eTest.Common.model.user.UserMaster;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static com.tmax.eTest.Common.model.user.QUserMaster.userMaster;
import static com.tmax.eTest.LRS.model.QStatement.statement;

@Repository
public class UserDeleteTimeRepository extends UserFilterRepository {
    private final JPAQueryFactory query;

    public UserDeleteTimeRepository(EntityManager entityManager) {
        this.query = new JPAQueryFactory(entityManager);
    }

    public List<UserMaster> filter(FilterQueryDTO filterQueryDTO) {
        return query.select(userMaster)
                .from(userMaster)
                .join(statement).on(statement.userId.eq(userMaster.userUuid))
                .where(
                        dateFilter(filterQueryDTO.getDateFrom(), filterQueryDTO.getDateTo()),
                        genderFilter(filterQueryDTO.getGender()),
                        ageGroupFilter(filterQueryDTO.getAgeGroupLowerBound(), filterQueryDTO.getAgeGroupUpperBound())
                )
                .fetch();
    }

    private BooleanExpression dateFilter(Timestamp dateFrom, Timestamp dateTo){
        LocalDateTime dateFromLDT = dateFrom.toLocalDateTime();
        LocalDateTime dateToLDT = dateTo.toLocalDateTime();
        if (dateFrom == null & dateTo == null)
            return null;
        return userMaster.createDate.between(dateFromLDT, dateToLDT);
    }
}
