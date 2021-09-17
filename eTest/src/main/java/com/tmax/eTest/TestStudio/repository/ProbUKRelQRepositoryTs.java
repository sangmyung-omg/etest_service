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
import com.tmax.eTest.Common.model.uk.QProblemUKRelation;

@Repository
@Transactional
public class ProbUKRelQRepositoryTs {
	
	@PersistenceContext
	private EntityManager em;
	private JPAQueryFactory queryFactory;
	
	public ProbUKRelQRepositoryTs(EntityManager em) {
		this.em = em;
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	public Long probUKRelDeleteAllByProbId(Integer probID) {

		QProblemUKRelation qProblemUKRelation = QProblemUKRelation.problemUKRelation;
		Long count = queryFactory.delete(qProblemUKRelation)
										.where(qProblemUKRelation.probID.probID.eq(probID))
										.execute();
										
		return count;
	}
	
	public Long probUKRelDeleteByProbIdAndUkIDs(Integer probID, List<Long> ukIDList) {

		QProblemUKRelation qProblemUKRelation = QProblemUKRelation.problemUKRelation;
		Long count = queryFactory.delete(qProblemUKRelation)
				.where(qProblemUKRelation.probIDOnly.eq(probID), qProblemUKRelation.ukIDOnly.longValue().in(ukIDList) )
				.execute();

		return count;
	}
	
	
	
}
