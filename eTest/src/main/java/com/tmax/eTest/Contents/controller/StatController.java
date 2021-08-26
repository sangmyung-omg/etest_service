package com.tmax.eTest.Contents.controller;

import javax.validation.Valid;

import com.tmax.eTest.Contents.dto.DateDTO;
import com.tmax.eTest.Contents.dto.UserListDTO;
import com.tmax.eTest.Contents.service.StatService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class StatController {

  @Autowired
  StatService statService;

  @GetMapping("/stats/contents/byDate")
  public ResponseEntity<Object> getContentsStatsByDate(@Valid DateDTO date) {
    log.info("---getContentsStatsByDate---");
    return new ResponseEntity<>(statService.getContentsStatsByDate(date), HttpStatus.OK);
  }

  @GetMapping("/stats/contents/byUsers")
  public ResponseEntity<Object> getContentsStatsByUsers(@Valid @RequestBody UserListDTO users) {
    log.info("---getContentsStatsByUsers---");
    return new ResponseEntity<>(statService.getContentsStatsByUsers(users), HttpStatus.OK);
  }
}
