package com.tmax.eTest.Contents.repository.support;

import static com.tmax.eTest.Common.model.report.QDiagnosisReport.diagnosisReport;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tmax.eTest.Common.model.report.DiagnosisReport;
import com.tmax.eTest.Contents.util.CommonUtils;
import com.tmax.eTest.Contents.util.QuerydslUtils;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class DiagnosisReportRepositorySupport extends QuerydslRepositorySupport {
  private final JPAQueryFactory query;

  public DiagnosisReportRepositorySupport(JPAQueryFactory query) {
    super(DiagnosisReport.class);
    this.query = query;
  }

  public OrderSpecifier<?> getDiagnosisReportSortedColumn() {
    return QuerydslUtils.getSortedColumn(Order.DESC, diagnosisReport, "diagnosisDate");
  }

  public DiagnosisReport findDiagnosisReportByUser(String userId) {
    return query.selectFrom(diagnosisReport).where(userEq(userId)).orderBy(getDiagnosisReportSortedColumn()).fetchOne();
  }

  private BooleanExpression userEq(String userId) {
    return CommonUtils.stringNullCheck(userId) ? null : diagnosisReport.userUuid.eq(userId);
  }
}
