package com.tmax.eTest.Contents.repository.support;

import static com.tmax.eTest.Common.model.book.QBook.book;
import static com.tmax.eTest.Common.model.book.QBookBookmark.bookBookmark;

import java.util.List;
import java.util.stream.Collectors;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tmax.eTest.Common.model.book.Book;
import com.tmax.eTest.Contents.dto.BookJoin;
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

  public List<BookJoin> findBooksByUser(String userId, String keyword) {
    return tupleToJoin(query.select(book, bookBookmark.userUuid).from(book).leftJoin(book.bookBookmarks, bookBookmark)
        .on(userEq(userId)).where(checkKeyword(keyword)).orderBy().fetch());
  }

  public List<BookJoin> findBookmarkBooksByUser(String userId, String keyword) {
    return tupleToJoin(query.select(book, bookBookmark.userUuid).from(book).join(book.bookBookmarks, bookBookmark)
        .where(userEq(userId)).where(checkKeyword(keyword)).orderBy().fetch());
  }

  private BooleanExpression userEq(String userId) {
    return CommonUtils.stringNullCheck(userId) ? null : bookBookmark.userUuid.eq(userId);
  }

  private BooleanExpression checkKeyword(String keyword) {
    return CommonUtils.stringNullCheck(keyword) ? null : book.title.contains(keyword);
  }

  private List<BookJoin> tupleToJoin(List<Tuple> tuples) {
    return tuples.stream().map(tuple -> new BookJoin(tuple.get(book), tuple.get(bookBookmark.userUuid)))
        .collect(Collectors.toList());
  }

}
