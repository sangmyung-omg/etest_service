package com.tmax.eTest.Report.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.tmax.eTest.Contents.model.ProblemUKRelation;

// Problem UK Relation Repository
public interface ProbUKRelRepo 
	extends CrudRepository<ProblemUKRelation, String>, 
			JpaSpecificationExecutor<ProblemUKRelation> {

}
