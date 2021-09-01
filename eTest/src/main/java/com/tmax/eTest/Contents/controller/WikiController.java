package com.tmax.eTest.Contents.controller;

import java.text.ParseException;

import com.tmax.eTest.Contents.service.WikiService;

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
public class WikiController {

  @Autowired
  private WikiService wikiService;

  @GetMapping("/users/{user_id}/wikis/{wiki_id}")
  public ResponseEntity<Object> getWiki(@PathVariable("user_id") String userId, @PathVariable("wiki_id") Long wikiId) {
    log.info("---getWiki---");
    return new ResponseEntity<>(wikiService.getWiki(userId, wikiId), HttpStatus.OK);
  }

  @GetMapping("/users/{user_id}/wikis")
  public ResponseEntity<Object> getWikiList(@PathVariable("user_id") String userId,
      @RequestParam(value = "keyword", required = false) String keyword) {
    log.info("---getWikiList---");
    return new ResponseEntity<>(wikiService.getWikiList(userId, keyword), HttpStatus.OK);
  }

  @GetMapping("/users/{user_id}/wikis/bookmark")
  public ResponseEntity<Object> getBookmarkBookList(@PathVariable("user_id") String userId,
      @RequestParam(value = "keyword", required = false) String keyword) {
    log.info("---getBookmarkWikiList---");
    return new ResponseEntity<>(wikiService.getBookmarkWikiList(userId, keyword), HttpStatus.OK);
  }

  @PutMapping("/users/{user_id}/wikis/{wiki_id}/bookmark")
  public ResponseEntity<Object> insertBookmarkWiki(@PathVariable("user_id") String userId,
      @PathVariable("wiki_id") Long wikiId) {
    log.info("---insertBookmarkWiki---");
    return new ResponseEntity<>(wikiService.insertBookmarkWiki(userId, wikiId), HttpStatus.OK);
  }

  @DeleteMapping("/users/{user_id}/wikis/{wiki_id}/bookmark")
  public ResponseEntity<Object> deleteBookmarkBook(@PathVariable("user_id") String userId,
      @PathVariable("wiki_id") Long wikiId) {
    log.info("---deleteBookmarkWiki---");
    return new ResponseEntity<>(wikiService.deleteBookmarkWiki(userId, wikiId), HttpStatus.OK);
  }

  @PostMapping("/users/{user_id}/wikis/{wiki_id}/hit")
  public ResponseEntity<Object> updateWikiHit(@PathVariable("user_id") String userId,
      @PathVariable("wiki_id") Long wikiId) throws ParseException {
    log.info("---updateWikiHit---");
    return new ResponseEntity<>(wikiService.updateWikiHit(userId, wikiId), HttpStatus.OK);
  }

  @PostMapping("/users/{user_id}/wikis/{wiki_id}/quit")
  public ResponseEntity<Object> quitWiki(@PathVariable("user_id") String userId, @PathVariable("wiki_id") Long wikiId,
      @RequestParam(value = "duration", required = true) Integer duration) throws ParseException {
    log.info("---quitWiki---");
    return new ResponseEntity<>(wikiService.quitWiki(userId, wikiId, duration), HttpStatus.OK);
  }
}
