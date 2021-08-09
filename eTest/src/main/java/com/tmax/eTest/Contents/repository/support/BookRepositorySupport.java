package com.tmax.eTest.Contents.repository.support;

import static com.tmax.eTest.Common.model.book.QBook.book;
import static com.tmax.eTest.Common.model.book.QBookBookmark.bookBookmark;

import java.util.List;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tmax.eTest.Common.model.book.Book;
import com.tmax.eTest.Contents.util.CommonUtils;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;


@Repository
public class BookRepositorySupport extends QuerydslRepositorySupport {
  private final JPAQueryFactory query;

  public BookRepositorySupport(JPAQueryFactory query) {
    super(Book.class);
    this.query = query;
  }

  public List<Book> findBooksByUser(String userId, String keyword) {
    return query.selectFrom(book).leftJoin(book.bookBookmarks, bookBookmark).on(userEq(userId))
        .where(checkKeyword(keyword)).orderBy().fetch();
  }

  public List<Book> findBookmarkBooksByUser(String userId, String keyword) {
    return query.selectFrom(book).join(book.bookBookmarks, bookBookmark)
        .where(userEq(userId)).where(checkKeyword(keyword))
        .orderBy().fetch();
  }

  private BooleanExpression userEq(String userId) {
    return CommonUtils.stringNullCheck(userId) ? null : bookBookmark.userUuid.eq(userId);
  }

  private BooleanExpression checkKeyword(String keyword) {
    return CommonUtils.stringNullCheck(keyword) ? null
        : book.title.contains(keyword);
  }

}
