package com.tmax.eTest.TestStudio.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.Common.model.problem.QProblem; 

@Repository
public class ProblemQRepositoryETest {

	@PersistenceContext
	private EntityManager em;
	private JPAQueryFactory queryFactory;
	
	public ProblemQRepositoryETest(EntityManager em) {
		this.em = em;
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	public Problem searchLatestProblem() {
		
		QProblem qProblem = new QProblem("qProblem");
		Problem problem = queryFactory.selectFrom(qProblem)
										.orderBy(qProblem.probID.desc())
										.fetchFirst();
	
		return problem;

	}

}
