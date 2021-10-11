package com.tmax.eTest.Contents.controller.exception;

import java.io.IOException;
import java.text.ParseException;

import com.tmax.eTest.Contents.dto.GenericErrorDTO;
import com.tmax.eTest.Contents.exception.ContentsException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice("com.tmax.eTest.Contents.controller")
public class ControllerExceptionAdvice {

  @ExceptionHandler({ IOException.class })
  public ResponseEntity<GenericErrorDTO> handleIOException(IOException exception) {
    log.error("INTERNAL_SERVER_ERROR: " + exception.getMessage());
    return new ResponseEntity<>(new GenericErrorDTO(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler({ ParseException.class })
  public ResponseEntity<GenericErrorDTO> handleParseException(ParseException exception) {
    log.error("PARSE_EXCEPTION: " + exception.getMessage());
    return new ResponseEntity<>(new GenericErrorDTO(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler({ IllegalArgumentException.class })
  public ResponseEntity<GenericErrorDTO> handleIllegalArgumentException(IllegalArgumentException exception) {
    log.error("BAD_REQUEST: " + exception.getMessage());
    return new ResponseEntity<>(new GenericErrorDTO(exception.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({ MethodArgumentNotValidException.class })
  public ResponseEntity<GenericErrorDTO> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException exception) {
    log.error("BAD_REQUEST: " + exception.getMessage());
    return new ResponseEntity<>(new GenericErrorDTO(exception.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
  public ResponseEntity<GenericErrorDTO> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException exception) {
    log.error("BAD_REQUEST: " + exception.getMessage());
    return new ResponseEntity<>(new GenericErrorDTO(exception.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({ MissingServletRequestParameterException.class })
  public ResponseEntity<GenericErrorDTO> handleMissingServletRequestParameterException(
      MissingServletRequestParameterException exception) {
    log.error("BAD_REQUEST: " + exception.getMessage());
    return new ResponseEntity<>(new GenericErrorDTO(exception.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
  public ResponseEntity<GenericErrorDTO> handleHttpRequestMethodNotSupportedException(
      HttpRequestMethodNotSupportedException exception) {
    log.error("METHOD_NOT_ALLOWED: " + exception.getMessage());
    return new ResponseEntity<>(new GenericErrorDTO(exception.getMessage()), HttpStatus.METHOD_NOT_ALLOWED);
  }

  @ExceptionHandler({ ContentsException.class })
  public ResponseEntity<GenericErrorDTO> handleContentsException(ContentsException exception) {
    log.error(exception.getStatus() + ": " + exception.getMessage());
    return new ResponseEntity<>(new GenericErrorDTO(exception.getErrorCode(), exception.getMessage()),
        exception.getStatus());
  }

}
