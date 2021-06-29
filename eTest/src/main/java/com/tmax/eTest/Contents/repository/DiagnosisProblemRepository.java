package com.tmax.eTest.Contents.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tmax.eTest.Contents.model.DiagnosisProblem;
import com.tmax.eTest.Contents.model.TestProblem;

public interface DiagnosisProblemRepository extends JpaRepository<DiagnosisProblem, Long>{
	@Query(value="select d from DiagnosisProblem d where d.subject = :subject")
	public ArrayList<DiagnosisProblem>findDiagnosisProblems(@Param("subject") int setNum);
	
	@Query("SELECT dp.probID FROM DiagnosisProblem dp WHERE dp.problem.category = ?1")
	List<Integer> findTendencyProblems(String queryType);
}
