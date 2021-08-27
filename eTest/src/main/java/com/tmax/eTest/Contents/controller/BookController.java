package com.tmax.eTest.Contents.controller;

import com.tmax.eTest.Contents.service.BookService;

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

  @GetMapping("/users/{user_id}/books/{book_id}")
  public ResponseEntity<Object> getBook(@PathVariable("user_id") String userId, @PathVariable("book_id") Long bookId) {
    log.info("---getBook---");
    return new ResponseEntity<>(bookService.getBook(userId, bookId), HttpStatus.OK);
  }

  @GetMapping("/users/{user_id}/books")
  public ResponseEntity<Object> getBookList(@PathVariable("user_id") String userId,
      @RequestParam(value = "keyword", required = false) String keyword) {
    log.info("---getBookList---");
    return new ResponseEntity<>(bookService.getBookList(userId, keyword), HttpStatus.OK);
  }

  @GetMapping("/users/{user_id}/books/bookmark")
  public ResponseEntity<Object> getBookmarkBookList(@PathVariable("user_id") String userId,
      @RequestParam(value = "keyword", required = false) String keyword) {
    log.info("---getBookmarkBookList---");
    return new ResponseEntity<>(bookService.getBookmarkBookList(userId, keyword), HttpStatus.OK);
  }

  @PutMapping("/users/{user_id}/books/{book_id}/bookmark")
  public ResponseEntity<Object> insertBookmarkBook(@PathVariable("user_id") String userId,
      @PathVariable("book_id") Long bookId) {
    log.info("---insertBookmarkBook---");
    return new ResponseEntity<>(bookService.insertBookmarkBook(userId, bookId), HttpStatus.OK);
  }

  @DeleteMapping("/users/{user_id}/books/{book_id}/bookmark")
  public ResponseEntity<Object> deleteBookmarkBook(@PathVariable("user_id") String userId,
      @PathVariable("book_id") Long bookId) {
    log.info("---deleteBookmarkBook---");
    return new ResponseEntity<>(bookService.deleteBookmarkBook(userId, bookId), HttpStatus.OK);
  }

  @PostMapping("/users/{user_id}/books/{book_id}/hit")
  public ResponseEntity<Object> updateBookHit(@PathVariable("user_id") String userId,
      @PathVariable("book_id") Long bookId) {
    log.info("---updateBookHit---");
    return new ResponseEntity<>(bookService.updateBookHit(userId, bookId), HttpStatus.OK);
  }
}
