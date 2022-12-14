package com.tmax.eTest.Contents.repository.support;

import static com.tmax.eTest.Common.model.book.QBook.book;
import static com.tmax.eTest.Common.model.book.QBookBookmark.bookBookmark;

import java.util.List;
import java.util.stream.Collectors;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tmax.eTest.Common.model.book.Book;
import com.tmax.eTest.Contents.dto.BookJoin;
import com.tmax.eTest.Contents.util.CommonUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class BookRepositorySupport extends QuerydslRepositorySupport {
  private final JPAQueryFactory query;

  @Autowired
  private CommonUtils commonUtils;

  public BookRepositorySupport(JPAQueryFactory query) {
    super(Book.class);
    this.query = query;
  }

  public JPAQuery<Book> multipleFetchJoin() {
    return query.select(book).from(book).join(book.bookHit).fetchJoin();
  }

  public JPAQuery<Tuple> multipleBookmarkFetchJoin() {
    return query.select(book, bookBookmark.userUuid).from(book).join(book.bookHit).fetchJoin()
        .leftJoin(book.bookBookmarks, bookBookmark).fetchJoin();
  }

  public Book findBookById(String bookId) {
    return multipleFetchJoin().where(idEq(bookId)).distinct().fetchOne();
  }

  public List<Book> findBooks(String keyword) {
    return multipleFetchJoin().where(checkKeyword(keyword)).orderBy().distinct().fetch();
  }

  public BookJoin findBookByUserAndId(String userId, String bookId) {
    return tupleToJoin(multipleBookmarkFetchJoin().leftJoin(book.bookBookmarks, bookBookmark).on(userEq(userId))
        .where(idEq(bookId)).fetch().stream().distinct().collect(Collectors.toList()).get(0));
  }

  public List<BookJoin> findBooksByUser(String userId, String keyword) {
    return tupleToJoin(multipleBookmarkFetchJoin().leftJoin(book.bookBookmarks, bookBookmark).on(userEq(userId))
        .where(checkKeyword(keyword)).orderBy().fetch().stream().distinct().collect(Collectors.toList()));
  }

  public List<BookJoin> findBookmarkBooksByUser(String userId, String keyword) {
    return tupleToJoin(multipleBookmarkFetchJoin().join(book.bookBookmarks, bookBookmark).where(userEq(userId))
        .where(checkKeyword(keyword)).orderBy().fetch().stream().distinct().collect(Collectors.toList()));
  }

  private BooleanExpression userEq(String userId) {
    return commonUtils.stringNullCheck(userId) ? null : bookBookmark.userUuid.eq(userId);
  }

  private BooleanExpression idEq(String bookId) {
    return commonUtils.objectNullcheck(bookId) ? null : book.bookId.eq(bookId);
  }

  private BooleanExpression checkKeyword(String keyword) {
    return commonUtils.stringNullCheck(keyword) ? null : book.title.containsIgnoreCase(keyword);
  }

  private BookJoin tupleToJoin(Tuple tuple) {
    return new BookJoin(tuple.get(book), tuple.get(bookBookmark.userUuid));
  }

  private List<BookJoin> tupleToJoin(List<Tuple> tuples) {
    return tuples.stream().map(tuple -> new BookJoin(tuple.get(book), tuple.get(bookBookmark.userUuid)))
        .collect(Collectors.toList());
  }

}
