package com.tmax.eTest.Admin.dashboard.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.tmax.eTest.Admin.dashboard.dto.FilterDTO;
import com.tmax.eTest.Auth.dto.Gender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

import static com.tmax.eTest.Common.model.user.QUserMaster.userMaster;

public class UserFilterRepository {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    public BooleanExpression genderFilter(String gender){
        if (gender == null)
            return null;
        return userMaster.gender.eq(Gender.valueOf(gender));
    }

    public BooleanExpression ageGroupFilter(LocalDate ageGroupLowerBound, LocalDate ageGroupUpperBound){
        if (ageGroupLowerBound == null & ageGroupUpperBound == null)
            return null;
        return userMaster.birthday.between(ageGroupLowerBound, ageGroupUpperBound);
    }
    public BooleanExpression investmentExperienceFilter(int investmentExperience){
        if (investmentExperience == 0 ){
            return null;
        }
        return userMaster.investPeriod.eq(investmentExperience);
    }


}
