package com.tmax.eTest.Contents.repository.support;

import static com.tmax.eTest.Common.model.book.QBookHit.bookHit;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tmax.eTest.Common.model.book.BookHit;
import com.tmax.eTest.Contents.util.CommonUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class BookHitRepositorySupport extends QuerydslRepositorySupport {
  private final JPAQueryFactory query;

  @Autowired
  private CommonUtils commonUtils;

  public BookHitRepositorySupport(JPAQueryFactory query) {
    super(BookHit.class);
    this.query = query;
  }

  // 애매한뎅
  public Boolean notExistsById(String bookId) {
    return commonUtils.objectNullcheck(query.selectOne().from(bookHit).where(bookHit.bookId.eq(bookId)).fetchFirst());
  }

  public Long updateVideoHit(String bookId) {
    return query.update(bookHit).set(bookHit.hit, bookHit.hit.add(1)).where(bookHit.bookId.eq(bookId)).execute();
  }
}
