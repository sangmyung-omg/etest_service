package com.tmax.eTest.TestStudio.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.Common.model.problem.QProblem;
import com.tmax.eTest.Common.model.problem.QProblemChoice;

@Repository
@Transactional
public class ProbChoiceQRepositoryETest {
	
	@PersistenceContext
	private EntityManager em;
	private JPAQueryFactory queryFactory;
	
	public ProbChoiceQRepositoryETest(EntityManager em) {
		this.em = em;
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	public Long probChoiceDeleteByProbId(Integer probID) {

		QProblemChoice qProblemChoice = QProblemChoice.problemChoice;
		Long count = queryFactory.delete(qProblemChoice)
										.where(qProblemChoice.probIDOnly.eq(probID))
										.execute();
										
		return count;
	}
	
}
