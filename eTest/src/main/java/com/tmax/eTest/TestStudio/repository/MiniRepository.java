package com.tmax.eTest.TestStudio.repository;

import static com.tmax.eTest.Common.model.problem.QDiagnosisProblem.diagnosisProblem;
import static com.tmax.eTest.Common.model.problem.QPart.part;
import static com.tmax.eTest.Common.model.problem.QProblem.problem;
import static com.tmax.eTest.Common.model.problem.QTestProblem.testProblem;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tmax.eTest.TestStudio.dto.MiniProblemListDTOContents;
import com.tmax.eTest.TestStudio.dto.QMiniProblemListDTOContents;

import org.apache.http.util.TextUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class MiniRepository {

	@PersistenceContext
	private EntityManager em;
	private JPAQueryFactory queryFactory;

	public MiniRepository(EntityManager em) {
		this.em = em;
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	public Page<MiniProblemListDTOContents> searchMiniProblems(Integer partId, String keyword, String order, String orderOption,
			Pageable pageable) {
		Path<Date> editDate = Expressions.datePath(Date.class, "editDate");
		JPAQuery<MiniProblemListDTOContents> query = queryFactory.select(new QMiniProblemListDTOContents(testProblem.probID, part.partName, problem.difficulty, testProblem.subject, testProblem.status, problem.question, problem.editDate.coalesce(problem.createDate).as(editDate)))
				.from(testProblem)
				.join(testProblem.problem, problem)
				.leftJoin(testProblem.part, part)
				.leftJoin(problem.diagnosisInfo, diagnosisProblem)
				.where(partId(partId), keyword(keyword))
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize());
		if (TextUtils.isEmpty(order)) {
			query.orderBy(testProblem.part.partName.asc(), testProblem.problem.difficulty.asc(), testProblem.status.asc(),
					((ComparableExpressionBase<Date>) editDate).asc());
		} else {
			query.orderBy(order(order, orderOption, editDate));
		}
		QueryResults<MiniProblemListDTOContents> results = query.fetchResults();
		return new PageImpl<>(results.getResults(), pageable, results.getTotal());
	}

	private BooleanExpression partId(Integer partId) {
		return partId == null ? null : testProblem.part.partID.eq(partId);
	}

	private BooleanExpression keyword(String keyword) {
		return TextUtils.isEmpty(keyword) ? null
				: testProblem.problem.question.contains(keyword).or(testProblem.problem.solution.contains(keyword))
						.or(testProblem.problem.questionInitial.contains(keyword))
						.or(testProblem.problem.solutionInitial.contains(keyword));
	}

	private OrderSpecifier<?> order(String order, String orderOption, Path<Date> editDate) {
		if (order.equals("part"))
			return orderOption.equals("descending") ? testProblem.part.partName.desc() : testProblem.part.partName.asc();
		if (order.equals("difficulty"))
			return orderOption.equals("descending") ? testProblem.problem.difficulty.desc()
					: testProblem.problem.difficulty.asc();
		if (order.equals("status"))
			return orderOption.equals("descending") ? testProblem.status.desc() : testProblem.status.asc();
		if (order.equals("editDate"))
			return orderOption.equals("descending") ? ((ComparableExpressionBase<Date>) editDate).desc()
					: ((ComparableExpressionBase<Date>) editDate).asc();
		return testProblem.part.partName.asc();
	}

}