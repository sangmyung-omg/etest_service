package com.tmax.eTest.TestStudio.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tmax.eTest.Common.model.problem.QProblemChoice;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ProbChoiceQRepositoryTs {

	@PersistenceContext
	private EntityManager em;
	private JPAQueryFactory queryFactory;

	public ProbChoiceQRepositoryTs(EntityManager em) {
		this.em = em;
		this.queryFactory = new JPAQueryFactory(em);
	}

	public Long probChoiceDeleteByProbId(Integer probID) {

		QProblemChoice qProblemChoice = QProblemChoice.problemChoice;
		Long count = queryFactory.delete(qProblemChoice).where(qProblemChoice.probIDOnly.eq(probID)).execute();

		return count;
	}

}
