package com.tmax.eTest.Contents.repository.support;

import static com.tmax.eTest.Common.model.stat.QHitStat.hitStat;

import java.util.List;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tmax.eTest.Common.model.stat.HitStat;
import com.tmax.eTest.Contents.dto.DateDTO;
import com.tmax.eTest.Contents.util.CommonUtils;
import com.tmax.eTest.Contents.util.QuerydslUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class HitStatRepositorySupport extends QuerydslRepositorySupport {
  private final JPAQueryFactory query;

  @Autowired
  private CommonUtils commonUtils;

  @Autowired
  private QuerydslUtils querydslUtils;

  public HitStatRepositorySupport(JPAQueryFactory query) {
    super(HitStat.class);
    this.query = query;
  }

  public List<HitStat> findAllByDate(DateDTO date) {
    return query.selectFrom(hitStat).where(cmpDate(date)).orderBy(sortByDate()).fetch();
  }

  public OrderSpecifier<?> sortByDate() {
    return querydslUtils.getSortedColumn(Order.ASC, hitStat, "statDate");
  }

  private BooleanExpression cmpDate(DateDTO date) {
    if (commonUtils.objectNullcheck(date))
      return null;
    return hitStat.statDate.between(date.getDateFrom(), date.getDateTo());
  }
}
