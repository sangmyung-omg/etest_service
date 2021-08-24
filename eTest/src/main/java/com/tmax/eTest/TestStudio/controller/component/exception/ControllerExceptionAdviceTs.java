package com.tmax.eTest.TestStudio.controller.component.exception;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice("com.tmax.eTest.TestStudio.controller")
public class ControllerExceptionAdviceTs {

  @ExceptionHandler({ IOException.class })
  public ResponseEntity<GenericError__DTO> handleIOError(IOException exception) {
    log.error("INTERNAL_SERVER_ERROR: " + exception.getMessage());
    return new ResponseEntity<>(new GenericError__DTO(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler({ IllegalArgumentException.class })
  public ResponseEntity<GenericError__DTO> handleIllegalArgumentError(IllegalArgumentException exception) {
    log.error("BAD_REQUEST: " + exception.getMessage());
    return new ResponseEntity<>(new GenericError__DTO(exception.getMessage(),HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({ CustomExceptionTs.class })
  public ResponseEntity<ErrorResponseTs> handleCustomException(CustomExceptionTs exception) {
    log.error( exception.getErrorCodeEnum().getHttpStatus() + ": " + exception.getErrorCodeEnum().getDetail());
    return ErrorResponseTs.toResponseEntity(exception.getErrorCodeEnum());
  }
  
  @ExceptionHandler
  public ResponseEntity<GenericError__DTO> basicExceptionProcess(Exception exception) {
    log.error( exception.getMessage() );
    return new ResponseEntity<>(new GenericError__DTO(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
