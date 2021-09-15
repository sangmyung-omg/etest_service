package com.tmax.eTest.Contents.controller;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import com.tmax.eTest.Contents.exception.ContentsException;
import com.tmax.eTest.Contents.exception.ErrorCode;
import com.tmax.eTest.Contents.service.BookService;
import com.tmax.eTest.Contents.util.CommonUtils;
import com.tmax.eTest.Contents.util.JWTUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class BookController {

  @Autowired
  private BookService bookService;

  @Autowired
  private CommonUtils commonUtils;

  @Autowired
  private JWTUtils jwtUtils;

  @GetMapping("/books")
  public ResponseEntity<Object> getBookList(@RequestParam(value = "keyword", required = false) String keyword,
      HttpServletRequest request) {
    log.info("---getBookList---");
    String userId = jwtUtils.getUserId(request);
    return new ResponseEntity<>(commonUtils.stringNullCheck(userId) ? bookService.getBookList(keyword)
        : bookService.getBookList(userId, keyword), HttpStatus.OK);
  }

  @GetMapping("/books/{book_id}")
  public ResponseEntity<Object> getBook(@PathVariable("book_id") String bookId, HttpServletRequest request) {
    log.info("---getBook---");
    String userId = jwtUtils.getUserId(request);
    return new ResponseEntity<>(
        commonUtils.stringNullCheck(userId) ? bookService.getBook(bookId) : bookService.getBook(userId, bookId),
        HttpStatus.OK);
  }

  @PostMapping("/books/{book_id}/hit")
  public ResponseEntity<Object> updateBookHit(@PathVariable("book_id") String bookId, HttpServletRequest request)
      throws ParseException {
    log.info("---updateBookHit---");
    String userId = jwtUtils.getUserId(request);
    return new ResponseEntity<>(commonUtils.stringNullCheck(userId) ? bookService.updateBookHit(bookId)
        : bookService.updateBookHit(userId, bookId), HttpStatus.OK);
  }

  @PostMapping("/books/{book_id}/quit")
  public ResponseEntity<Object> quitBook(@PathVariable("book_id") String bookId,
      @RequestParam(value = "duration", required = true) Integer duration, HttpServletRequest request)
      throws ParseException {
    log.info("---quitBook---");
    String userId = jwtUtils.getUserId(request);
    return new ResponseEntity<>(commonUtils.stringNullCheck(userId) ? bookService.quitBook(bookId, duration)
        : bookService.quitBook(userId, bookId, duration), HttpStatus.OK);
  }

  @GetMapping("/books/bookmark")
  public ResponseEntity<Object> getBookmarkBookList(@RequestParam(value = "keyword", required = false) String keyword,
      HttpServletRequest request) {
    log.info("---getBookmarkBookList---");
    String userId = jwtUtils.getUserId(request);
    if (commonUtils.stringNullCheck(userId))
      throw new ContentsException(ErrorCode.USER_ERROR);
    return new ResponseEntity<>(bookService.getBookmarkBookList(userId, keyword), HttpStatus.OK);
  }

  @PutMapping("/books/{book_id}/bookmark")
  public ResponseEntity<Object> insertBookmarkBook(@PathVariable("book_id") String bookId, HttpServletRequest request) {
    log.info("---insertBookmarkBook---");
    String userId = jwtUtils.getUserId(request);
    if (commonUtils.stringNullCheck(userId))
      throw new ContentsException(ErrorCode.USER_ERROR);
    return new ResponseEntity<>(bookService.insertBookmarkBook(userId, bookId), HttpStatus.OK);
  }

  @DeleteMapping("/books/{book_id}/bookmark")
  public ResponseEntity<Object> deleteBookmarkBook(@PathVariable("book_id") String bookId, HttpServletRequest request) {
    log.info("---deleteBookmarkBook---");
    String userId = jwtUtils.getUserId(request);
    if (commonUtils.stringNullCheck(userId))
      throw new ContentsException(ErrorCode.USER_ERROR);
    return new ResponseEntity<>(bookService.deleteBookmarkBook(userId, bookId), HttpStatus.OK);
  }
}
