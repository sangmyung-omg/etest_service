package com.tmax.eTest.Contents.service;

import java.util.List;
import java.util.stream.Collectors;

import com.tmax.eTest.Common.model.book.Book;
import com.tmax.eTest.Common.model.book.BookBookmark;
import com.tmax.eTest.Common.model.book.BookBookmarkId;
import com.tmax.eTest.Common.repository.book.BookBookmarkRepository;
import com.tmax.eTest.Contents.dto.BookDTO;
import com.tmax.eTest.Contents.dto.ListDTO;
import com.tmax.eTest.Contents.dto.SuccessDTO;
import com.tmax.eTest.Contents.exception.ContentsException;
import com.tmax.eTest.Contents.exception.ErrorCode;
import com.tmax.eTest.Contents.repository.support.BookRepositorySupport;
import com.tmax.eTest.Contents.util.CommonUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {

  @Autowired
  private BookBookmarkRepository bookBookmarkRepository;

  @Autowired
  private BookRepositorySupport bookRepositorySupport;

  public ListDTO.Book getBookList(String userId, String keyword) {
    List<Book> books = bookRepositorySupport.findBooksByUser(userId, keyword);
    return convertBookToDTO(books);
  }

  public ListDTO.Book getBookmarkBookList(String userId, String keyword) {
    List<Book> books = bookRepositorySupport.findBookmarkBooksByUser(userId, keyword);
    return convertBookToDTO(books);
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

  public ListDTO.Book convertBookToDTO(List<Book> books) {
    return new ListDTO.Book(books.size(),
        books.stream()
            .map(book -> new BookDTO(book.getBookId(), book.getBookSrc(), book.getTitle(),
                book.getCreateDate().toString(), book.getCreatorId(), book.getImgSrc(), book.getDescription(),
                !CommonUtils.objectNullcheck(book.getBookBookmarks())))
            .collect(Collectors.toList()));
  }
}
