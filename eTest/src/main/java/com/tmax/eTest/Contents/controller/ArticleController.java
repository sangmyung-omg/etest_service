package com.tmax.eTest.Contents.controller;

import java.text.ParseException;

import com.tmax.eTest.Contents.service.ArticleService;

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
public class ArticleController {

  @Autowired
  private ArticleService articleService;

  @GetMapping("/users/{user_id}/articles/{article_id}")
  public ResponseEntity<Object> getArticle(@PathVariable("user_id") String userId,
      @PathVariable("article_id") Long articleId) {
    log.info("---getArticle---");
    return new ResponseEntity<>(articleService.getArticle(userId, articleId), HttpStatus.OK);
  }

  @GetMapping("/users/{user_id}/articles")
  public ResponseEntity<Object> getArticleList(@PathVariable("user_id") String userId,
      @RequestParam(value = "keyword", required = false) String keyword) {
    log.info("---getArticleList---");
    return new ResponseEntity<>(articleService.getArticleList(userId, keyword), HttpStatus.OK);
  }

  @GetMapping("/users/{user_id}/articles/bookmark")
  public ResponseEntity<Object> getBookmarkArticleList(@PathVariable("user_id") String userId,
      @RequestParam(value = "keyword", required = false) String keyword) {
    log.info("---getBookmarkArticleList---");
    return new ResponseEntity<>(articleService.getBookmarkArticleList(userId, keyword), HttpStatus.OK);
  }

  @PutMapping("/users/{user_id}/articles/{article_id}/bookmark")
  public ResponseEntity<Object> insertBookmarkArticle(@PathVariable("user_id") String userId,
      @PathVariable("article_id") Long articleId) {
    log.info("---insertBookmarkArticle---");
    return new ResponseEntity<>(articleService.insertBookmarkArticle(userId, articleId), HttpStatus.OK);
  }

  @DeleteMapping("/users/{user_id}/articles/{article_id}/bookmark")
  public ResponseEntity<Object> deleteBookmarkArticle(@PathVariable("user_id") String userId,
      @PathVariable("article_id") Long articleId) {
    log.info("---deleteBookmarkArticle---");
    return new ResponseEntity<>(articleService.deleteBookmarkArticle(userId, articleId), HttpStatus.OK);
  }

  @PostMapping("/users/{user_id}/articles/{article_id}/hit")
  public ResponseEntity<Object> updateArticleHit(@PathVariable("user_id") String userId,
      @PathVariable("article_id") Long articleId) throws ParseException {
    log.info("---updateArticleHit---");
    return new ResponseEntity<>(articleService.updateArticleHit(userId, articleId), HttpStatus.OK);
  }

  @PostMapping("/users/{user_id}/articles/{article_id}/quit")
  public ResponseEntity<Object> quitArticle(@PathVariable("user_id") String userId,
      @PathVariable("article_id") Long articleId, @RequestParam(value = "duration", required = true) Integer duration)
      throws ParseException {
    log.info("---quitArticle---");
    return new ResponseEntity<>(articleService.quitArticle(userId, articleId, duration), HttpStatus.OK);
  }

}
