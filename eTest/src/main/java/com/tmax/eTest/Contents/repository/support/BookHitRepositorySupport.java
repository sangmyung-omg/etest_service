package com.tmax.eTest.Contents.repository.support;

import static com.tmax.eTest.Common.model.book.QBookHit.bookHit;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tmax.eTest.Common.model.book.BookHit;
import com.tmax.eTest.Contents.util.CommonUtils;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class BookHitRepositorySupport extends QuerydslRepositorySupport {
  private final JPAQueryFactory query;

  public BookHitRepositorySupport(JPAQueryFactory query) {
    super(BookHit.class);
    this.query = query;
  }

  // 애매한뎅
  public Boolean notExistsById(Long bookId) {
    return CommonUtils.objectNullcheck(query.selectOne().from(bookHit).where(bookHit.bookId.eq(bookId)).fetchFirst());
  }

  public Long updateVideoHit(Long bookId) {
    return query.update(bookHit).set(bookHit.hit, bookHit.hit.add(1)).where(bookHit.bookId.eq(bookId)).execute();
  }
}
