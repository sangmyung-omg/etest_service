package com.tmax.eTest.Contents.service;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.tmax.eTest.Common.model.book.Book;
import com.tmax.eTest.Common.model.book.BookBookmark;
import com.tmax.eTest.Common.model.book.BookBookmarkId;
import com.tmax.eTest.Common.repository.book.BookBookmarkRepository;
import com.tmax.eTest.Contents.dto.BookDTO;
import com.tmax.eTest.Contents.dto.BookJoin;
import com.tmax.eTest.Contents.dto.ListDTO;
import com.tmax.eTest.Contents.dto.SuccessDTO;
import com.tmax.eTest.Contents.exception.ContentsException;
import com.tmax.eTest.Contents.exception.ErrorCode;
import com.tmax.eTest.Contents.repository.support.BookHitRepositorySupport;
import com.tmax.eTest.Contents.repository.support.BookRepositorySupport;
import com.tmax.eTest.Contents.util.CommonUtils;
import com.tmax.eTest.Contents.util.LRSUtils;
import com.tmax.eTest.LRS.util.LRSAPIManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {

  @Autowired
  private BookBookmarkRepository bookBookmarkRepository;

  @Autowired
  private BookRepositorySupport bookRepositorySupport;

  @Autowired
  private BookHitRepositorySupport bookHitRepositorySupport;

  @Autowired
  private LRSUtils lrsUtils;

  @Autowired
  private LRSAPIManager lrsapiManager;

  public BookDTO getBook(String userId, Long bookId) {
    BookJoin book = bookRepositorySupport.findBookByUserAndId(userId, bookId);
    return convertBookJoinToDTO(book);
  }

  public ListDTO.Book getBookList(String keyword) {
    List<Book> books = bookRepositorySupport.findBooks(keyword);
    return convertBookToDTO(books);
  }

  public ListDTO.Book getBookList(String userId, String keyword) {
    List<BookJoin> books = bookRepositorySupport.findBooksByUser(userId, keyword);
    return convertBookJoinToDTO(books);
  }

  public ListDTO.Book getBookmarkBookList(String userId, String keyword) {
    List<BookJoin> books = bookRepositorySupport.findBookmarkBooksByUser(userId, keyword);
    return convertBookJoinToDTO(books);
  }

  @Transactional
  public SuccessDTO insertBookmarkBook(String userId, Long bookId) {
    BookBookmarkId bookBookmarkId = new BookBookmarkId(userId, bookId);
    BookBookmark bookBookmark = new BookBookmark(userId, bookId);
    if (bookBookmarkRepository.existsById(bookBookmarkId))
      throw new ContentsException(ErrorCode.DB_ERROR, "BookBookmark already exists in BookBookmark Table");
    else
      bookBookmarkRepository.save(bookBookmark);

    return new SuccessDTO(true);
  }

  @Transactional
  public SuccessDTO deleteBookmarkBook(String userId, Long bookId) {
    BookBookmarkId bookBookmarkId = new BookBookmarkId(userId, bookId);
    bookBookmarkRepository.delete(bookBookmarkRepository.findById(bookBookmarkId).orElseThrow(
        () -> new ContentsException(ErrorCode.DB_ERROR, "BookBookmark doesn't exist in BookBookmark Table")));

    return new SuccessDTO(true);
  }

  @Transactional
  public SuccessDTO updateBookHit(Long bookId) {
    if (bookHitRepositorySupport.notExistsById(bookId))
      throw new ContentsException(ErrorCode.DB_ERROR, "BookId doesn't exist in BookHit Table");
    bookHitRepositorySupport.updateVideoHit(bookId);

    return new SuccessDTO(true);
  }

  @Transactional
  public SuccessDTO updateBookHit(String userId, Long bookId) throws ParseException {
    if (bookHitRepositorySupport.notExistsById(bookId))
      throw new ContentsException(ErrorCode.DB_ERROR, "BookId doesn't exist in BookHit Table");
    bookHitRepositorySupport.updateVideoHit(bookId);

    lrsapiManager.saveStatementList(Arrays.asList(lrsUtils.makeStatement(userId, Long.toString(bookId),
        LRSUtils.ACTION_TYPE.enter, LRSUtils.SOURCE_TYPE.textbook)));

    return new SuccessDTO(true);
  }

  public SuccessDTO quitBook(String userId, Long bookId, Integer duration) throws ParseException {
    lrsapiManager.saveStatementList(Arrays.asList(lrsUtils.makeStatement(userId, Long.toString(bookId),
        LRSUtils.ACTION_TYPE.quit, LRSUtils.SOURCE_TYPE.textbook, duration)));
    return new SuccessDTO(true);
  }

  public BookDTO convertBookToDTO(Book book) {
    return BookDTO.builder().bookId(book.getBookId()).bookSrc(book.getBookSrc()).title(book.getTitle())
        .createDate(book.getCreateDate().toString()).creatorId(book.getCreatorId()).imgSrc(book.getImgSrc())
        .description(book.getDescription()).hit(book.getBookHit().getHit()).build();
  }

  public ListDTO.Book convertBookToDTO(List<Book> books) {
    return new ListDTO.Book(books.size(),
        books.stream().map(book -> convertBookToDTO(book)).collect(Collectors.toList()));
  }

  public BookDTO convertBookJoinToDTO(BookJoin bookJoin) {
    Book book = bookJoin.getBook();
    return BookDTO.builder().bookId(book.getBookId()).bookSrc(book.getBookSrc()).title(book.getTitle())
        .createDate(book.getCreateDate().toString()).creatorId(book.getCreatorId()).imgSrc(book.getImgSrc())
        .description(book.getDescription()).hit(book.getBookHit().getHit())
        .bookmark(!CommonUtils.stringNullCheck(bookJoin.getUserUuid())).build();
  }

  public ListDTO.Book convertBookJoinToDTO(List<BookJoin> bookJoins) {
    return new ListDTO.Book(bookJoins.size(),
        bookJoins.stream().map(bookJoin -> convertBookJoinToDTO(bookJoin)).collect(Collectors.toList()));
  }
}
