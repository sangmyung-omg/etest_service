package com.tmax.eTest.Event.controller;

import javax.servlet.http.HttpServletRequest;

import com.tmax.eTest.Contents.exception.ContentsException;
import com.tmax.eTest.Contents.exception.ErrorCode;
import com.tmax.eTest.Contents.util.CommonUtils;
import com.tmax.eTest.Contents.util.JWTUtils;
import com.tmax.eTest.Event.dto.EventDTO;
import com.tmax.eTest.Event.service.EventService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class EventController {

  @Autowired
  private JWTUtils jwtUtils;

  @Autowired
  private CommonUtils commonUtils;

  @Autowired
  private EventService eventService;

  @GetMapping("/event")
  public ResponseEntity<Object> getUserEventInfo(HttpServletRequest request) {
    log.info("---getUserEventInfo---");
    String userId = jwtUtils.getUserId(request);
    if (commonUtils.stringNullCheck(userId))
      throw new ContentsException(ErrorCode.USER_ERROR);
    return new ResponseEntity<>(eventService.getUserEventInfo(userId), HttpStatus.OK);
  }

  @PostMapping("/event")
  public ResponseEntity<Object> createUserEventInfo(HttpServletRequest request,
      @RequestBody EventDTO eventDTO) {
    log.info("---createUserEventInfo---");
    String userId = jwtUtils.getUserId(request);
    if (commonUtils.stringNullCheck(userId))
      throw new ContentsException(ErrorCode.USER_ERROR);
    return new ResponseEntity<>(eventService.createUserEventInfo(userId, eventDTO), HttpStatus.OK);
  }

  @PutMapping("/event")
  public ResponseEntity<Object> updateUserEventInfo(HttpServletRequest request,
      @RequestBody EventDTO eventDTO) {
    log.info("---updateUserEventInfo---");
    String userId = jwtUtils.getUserId(request);
    if (commonUtils.stringNullCheck(userId))
      throw new ContentsException(ErrorCode.USER_ERROR);
    return new ResponseEntity<>(eventService.updateUserEventInfo(userId, eventDTO), HttpStatus.OK);
  }

  @DeleteMapping("/event")
  public ResponseEntity<Object> deleteUserEventInfo(HttpServletRequest request) {
    log.info("---deleteUserEventInfo---");
    String userId = jwtUtils.getUserId(request);
    if (commonUtils.stringNullCheck(userId))
      throw new ContentsException(ErrorCode.USER_ERROR);
    return new ResponseEntity<>(eventService.deleteUserEventInfo(userId), HttpStatus.OK);
  }

}