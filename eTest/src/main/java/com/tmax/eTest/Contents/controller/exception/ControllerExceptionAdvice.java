package com.tmax.eTest.Contents.controller.exception;

import java.io.IOException;

import com.tmax.eTest.Contents.dto.ErrorDto;
import com.tmax.eTest.Contents.exception.ContentsException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice("com.tmax.eTest.Contents.controller")
public class ControllerExceptionAdvice {

  @ExceptionHandler({ IOException.class })
  public ResponseEntity<ErrorDto> handleIOError(IOException exception) {
    log.error("INTERNAL_SERVER_ERROR: " + exception.getMessage());
    return new ResponseEntity<>(new ErrorDto(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler({ IllegalArgumentException.class })
  public ResponseEntity<ErrorDto> handleIllegalArgumentError(IllegalArgumentException exception) {
    log.error("BAD_REQUEST: " + exception.getMessage());
    return new ResponseEntity<>(new ErrorDto(exception.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({ ContentsException.class })
  public ResponseEntity<ErrorDto> handleContentsError(ContentsException exception) {
    log.error(exception.getStatus() + ": " + exception.getMessage());
    return new ResponseEntity<>(new ErrorDto(exception.getErrorCode(), exception.getMessage()), exception.getStatus());
  }
}
