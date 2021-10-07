//package com.tmax.eTest.TestStudio.controller.test;
//
//import static com.tmax.eTest.Common.model.problem.QDiagnosisProblem.diagnosisProblem;
//import static com.tmax.eTest.Common.model.problem.QPart.part;
//import static com.tmax.eTest.Common.model.problem.QProblem.problem;
//import static com.tmax.eTest.Common.model.problem.QTestProblem.testProblem;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//
//import org.apache.http.util.TextUtils;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import com.querydsl.core.QueryResults;
//import com.querydsl.core.types.OrderSpecifier;
//import com.querydsl.core.types.dsl.BooleanExpression;
//import com.querydsl.jpa.JPAExpressions;
//import com.querydsl.jpa.impl.JPAQuery;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import com.tmax.eTest.Common.model.problem.Problem;
//import com.tmax.eTest.Common.model.problem.QProblem;
//import com.tmax.eTest.Common.model.problem.TestProblem;
//
//@Repository
//public class TestProblemQRepositoryETest {
//
//	@PersistenceContext
//	private EntityManager em;
//	private JPAQueryFactory queryFactory;
//	
//	public TestProblemQRepositoryETest(EntityManager em) {
//		this.em = em;
//		this.queryFactory = new JPAQueryFactory(em);
//	}
//	
//	public Problem searchLatestProblem() {
//		
//		JPAQuery query = new JPAQuery(em);
//		QProblem qProblem = new QProblem("qProblem");
//		Problem problem = queryFactory.selectFrom(qProblem)
//										.orderBy(qProblem.probID.desc())
//										.fetchFirst();
//		
//		return problem;
//
//	}
//	public Problem searchLatestProblem2() {
//		
//		QProblem qProblem = new QProblem("qProblem");
//		QProblem qProblem__ = new QProblem("qProblem__");
//		Problem problem = queryFactory.selectFrom(qProblem)
//										.where(qProblem.probID.eq(
//												JPAExpressions.select(qProblem__.probID.max()).from(qProblem__)))
//										.fetchFirst();
//		
//		return problem;
//
//	}
//	public Integer searchLatestProblemIDTest() {
//		
//		QProblem qProblem = new QProblem("qProblem");
//		Integer probID = queryFactory.selectFrom(qProblem)
//										.orderBy(qProblem.probID.desc())
//										.fetchFirst().getProbID();
//	
//		System.out.println(probID);
//		return probID;
//
//	}
//	
//	
//}
