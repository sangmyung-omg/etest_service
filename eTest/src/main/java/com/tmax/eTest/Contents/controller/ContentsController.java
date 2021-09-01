package com.tmax.eTest.Contents.controller;

import com.tmax.eTest.Contents.service.ContentsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ContentsController {

  @Autowired
  private ContentsService contentsService;

  @GetMapping("/users/{user_id}/contents/bookmark")
  public ResponseEntity<Object> getBookmarkList(@PathVariable("user_id") String userId) {
    log.info("---getBookmarkList---");
    return new ResponseEntity<>(contentsService.getBookmarkList(userId), HttpStatus.OK);
  }
}
