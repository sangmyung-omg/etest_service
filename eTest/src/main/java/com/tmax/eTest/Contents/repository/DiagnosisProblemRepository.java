package com.tmax.eTest.Contents.repository;

import java.util.ArrayList;
import java.util.List;

import com.tmax.eTest.Common.model.problem.DiagnosisProblem;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiagnosisProblemRepository extends JpaRepository<DiagnosisProblem, Integer>{
	@Query(value="select d from DiagnosisProblem d where d.subject = :subject")
	public ArrayList<DiagnosisProblem>findDiagnosisProblems(@Param("subject") String setNum);
	
	List<DiagnosisProblem> findByCurriculumIdOrderByOrderNumAsc(Integer CurriculumId);
}
