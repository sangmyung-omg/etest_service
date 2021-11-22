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

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookService {

  private BookBookmarkRepository bookBookmarkRepository;

  private BookRepositorySupport bookRepositorySupport;

  private BookHitRepositorySupport bookHitRepositorySupport;

  private LRSUtils lrsUtils;

  private LRSAPIManager lrsapiManager;

  private CommonUtils commonUtils;

  public BookService(BookBookmarkRepository bookBookmarkRepository, BookRepositorySupport bookRepositorySupport,
      BookHitRepositorySupport bookHitRepositorySupport, LRSUtils lrsUtils, CommonUtils commonUtils,
      LRSAPIManager lrsapiManager) {
    this.bookBookmarkRepository = bookBookmarkRepository;
    this.bookHitRepositorySupport = bookHitRepositorySupport;
    this.bookRepositorySupport = bookRepositorySupport;
    this.lrsUtils = lrsUtils;
    this.commonUtils = commonUtils;
    this.lrsapiManager = lrsapiManager;
  }

  public BookDTO getBook(String bookId) {
    Book book = bookRepositorySupport.findBookById(bookId);
    return convertBookToDTO(book);
  }

  public BookDTO getBook(String userId, String bookId) {
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
  public SuccessDTO insertBookmarkBook(String userId, String bookId) {
    BookBookmarkId bookBookmarkId = new BookBookmarkId(userId, bookId);
    BookBookmark bookBookmark = new BookBookmark(userId, bookId);
    if (bookBookmarkRepository.existsById(bookBookmarkId))
      throw new ContentsException(ErrorCode.DB_ERROR, "BookBookmark already exists in BookBookmark Table");
    else
      bookBookmarkRepository.save(bookBookmark);

    return new SuccessDTO(true);
  }

  @Transactional
  public SuccessDTO deleteBookmarkBook(String userId, String bookId) {
    BookBookmarkId bookBookmarkId = new BookBookmarkId(userId, bookId);
    bookBookmarkRepository.delete(bookBookmarkRepository.findById(bookBookmarkId).orElseThrow(
        () -> new ContentsException(ErrorCode.DB_ERROR, "BookBookmark doesn't exist in BookBookmark Table")));

    return new SuccessDTO(true);
  }

  @Transactional
  public SuccessDTO updateBookHit(String bookId) {
    if (bookHitRepositorySupport.notExistsById(bookId))
      throw new ContentsException(ErrorCode.DB_ERROR, "BookId doesn't exist in BookHit Table");
    bookHitRepositorySupport.updateVideoHit(bookId);

    return new SuccessDTO(true);
  }

  @Transactional
  public SuccessDTO updateBookHit(String userId, String bookId) throws ParseException {
    lrsapiManager.saveStatementList(Arrays
        .asList(lrsUtils.makeStatement(userId, bookId, LRSUtils.ACTION_TYPE.enter, LRSUtils.SOURCE_TYPE.textbook)));

    return updateBookHit(bookId);
  }

  public SuccessDTO quitBook(String bookId, Integer duration) {
    // lrsapiManager.saveStatementList(Arrays.asList(lrsUtils.makeStatement(userId,
    // Long.toString(bookId),
    // LRSUtils.ACTION_TYPE.quit, LRSUtils.SOURCE_TYPE.textbook, duration)));
    return new SuccessDTO(true);
  }

  public SuccessDTO quitBook(String userId, String bookId, Integer duration) throws ParseException {
    lrsapiManager.saveStatementList(Arrays.asList(
        lrsUtils.makeStatement(userId, bookId, LRSUtils.ACTION_TYPE.quit, LRSUtils.SOURCE_TYPE.textbook, duration)));
    return quitBook(bookId, duration);
  }

  public BookDTO convertBookToDTO(Book book) {
    return BookDTO.builder().bookId(book.getBookId()).bookSrc(book.getBookSrc()).title(book.getTitle())
        .createDate(book.getCreateDate().toString()).creatorId(book.getCreatorId()).imgSrc(book.getImgSrc())
        .description(book.getDescription()).hit(book.getBookHit().getHit()).pdf(book.getPdf()).build();
  }

  public ListDTO.Book convertBookToDTO(List<Book> books) {
    return new ListDTO.Book(books.size(),
        books.stream().map(book -> convertBookToDTO(book)).collect(Collectors.toList()));
  }

  public BookDTO convertBookJoinToDTO(BookJoin bookJoin) {
    Book book = bookJoin.getBook();
    BookDTO bookDTO = convertBookToDTO(book);
    bookDTO.setBookmark(!commonUtils.stringNullCheck(bookJoin.getUserUuid()));
    return bookDTO;
  }

  public ListDTO.Book convertBookJoinToDTO(List<BookJoin> bookJoins) {
    return new ListDTO.Book(bookJoins.size(),
        bookJoins.stream().map(bookJoin -> convertBookJoinToDTO(bookJoin)).collect(Collectors.toList()));
  }
}
