package com.tmax.eTest.Common.repository.uk;

import com.tmax.eTest.Common.model.uk.ProbUKCompositeKey;
import com.tmax.eTest.Common.model.uk.ProblemUKRelation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemUkReplationRepo extends JpaRepository<ProblemUKRelation, ProbUKCompositeKey>{
    
}
