package com.tmax.eTest.Common.repository.problem;

import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.Common.model.problem.ProblemChoice;
import com.tmax.eTest.Common.model.problem.ProblemChoiceCompositeKey;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemChoiceRepo extends JpaRepository<ProblemChoice, ProblemChoiceCompositeKey>{
    
}
