package com.tmax.eTest.Contents.repository.support;

import static com.tmax.eTest.Common.model.meta.QMetaCodeMaster.metaCodeMaster;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tmax.eTest.Common.model.meta.MetaCodeMaster;
import com.tmax.eTest.Contents.util.CommonUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class MetaCodeMasterRepositorySupport extends QuerydslRepositorySupport {

  private final JPAQueryFactory query;

  @Autowired
  private CommonUtils commonUtils;

  public MetaCodeMasterRepositorySupport(JPAQueryFactory query) {
    super(MetaCodeMaster.class);
    this.query = query;
  }

  public MetaCodeMaster findMetaCodeById(String metaCodeId) {
    return query.selectFrom(metaCodeMaster).where(idEq(metaCodeId)).fetchOne();
  }

  private BooleanExpression idEq(String metaCodeId) {
    return commonUtils.objectNullcheck(metaCodeId) ? null : metaCodeMaster.metaCodeId.eq(metaCodeId);
  }
}
