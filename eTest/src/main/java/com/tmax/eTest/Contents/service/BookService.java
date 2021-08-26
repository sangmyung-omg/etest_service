package com.tmax.eTest.Contents.service;

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
import com.tmax.eTest.Contents.repository.support.BookRepositorySupport;
import com.tmax.eTest.Contents.util.CommonUtils;
import com.tmax.eTest.Contents.util.LRSService;

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
  private LRSService lrsService;

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

  public SuccessDTO updateBookHit(String userId, Long bookId) {
    lrsService.init("/SaveStatement");
    lrsService.saveStatement(lrsService.makeStatement(userId, Long.toString(bookId), LRSService.ACTION_TYPE.enter,
        LRSService.SOURCE_TYPE.textbook));
    return new SuccessDTO(true);
  }

  // public ListDTO.Book convertBookToDTO(List<Book> books) {
  // return new ListDTO.Book(books.size(),
  // books.stream()
  // .map(book -> new BookDTO(book.getBookId(), book.getBookSrc(),
  // book.getTitle(),
  // book.getCreateDate().toString(), book.getCreatorId(), book.getImgSrc(),
  // book.getDescription(),
  // !CommonUtils.objectNullcheck(book.getBookBookmarks())))
  // .collect(Collectors.toList()));
  // }

  public ListDTO.Book convertBookJoinToDTO(List<BookJoin> bookJoins) {
    return new ListDTO.Book(bookJoins.size(), bookJoins.stream().map(bookJoin -> {
      Book book = bookJoin.getBook();
      return new BookDTO(book.getBookId(), book.getBookSrc(), book.getTitle(), book.getCreateDate().toString(),
          book.getCreatorId(), book.getImgSrc(), book.getDescription(),
          !CommonUtils.stringNullCheck(bookJoin.getUserUuid()));
    }).collect(Collectors.toList()));
  }
}
