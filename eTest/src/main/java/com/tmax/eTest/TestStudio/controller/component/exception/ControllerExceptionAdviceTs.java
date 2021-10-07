package com.tmax.eTest.TestStudio.controller.component.exception;

import java.io.FileNotFoundException;
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
//    log.error("INTERNAL_SERVER_ERROR: " + exception.getMessage());
	  log.error("INTERNAL_SERVER_ERROR: " + "IOException");
//    return new ResponseEntity<>(new GenericError__DTO(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    return new ResponseEntity<>(new GenericError__DTO("IOException",HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler({ IllegalArgumentException.class })
  public ResponseEntity<GenericError__DTO> handleIllegalArgumentError(IllegalArgumentException exception) {
//    log.error("BAD_REQUEST: " + exception.getMessage());
	  log.error("BAD_REQUEST: " + "IllegalArgumentException");
//    return new ResponseEntity<>(new GenericError__DTO(exception.getMessage(),HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    return new ResponseEntity<>(new GenericError__DTO("IllegalArgumentException",HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
  }
  
  @ExceptionHandler({ FileNotFoundException.class })
  public ResponseEntity<GenericError__DTO> handleIFileNotFoundError(FileNotFoundException exception) {
//    log.error("NOT_FOUND: " + exception.getMessage());
	  log.error("NOT_FOUND: " + "FileNotFoundException");
//    return new ResponseEntity<>(new GenericError__DTO(exception.getMessage(),HttpStatus.BAD_REQUEST), HttpStatus.NOT_FOUND);
    return new ResponseEntity<>(new GenericError__DTO("FileNotFoundException",HttpStatus.BAD_REQUEST), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler({ CustomExceptionTs.class })
  public ResponseEntity<ErrorResponseTs> handleCustomException(CustomExceptionTs exception) {
    log.error( exception.getErrorCodeEnum().getHttpStatus() + ": " + exception.getErrorCodeEnum().getDetail());
    return ErrorResponseTs.toResponseEntity(exception.getErrorCodeEnum());
  }
  
  @ExceptionHandler({ NoDataExceptionTs.class })
  public ResponseEntity<GenericError__DTO> handleCustomException_NoDataExceptionTs(NoDataExceptionTs exception) {
//    log.error( exception.getSecuredMessage() );
	  log.error("NOT_FOUND: " + "NoDataExceptionTs");
    return new ResponseEntity<>(new GenericError__DTO(exception.getSecuredMessage(),HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
  }
  
  @ExceptionHandler
  public ResponseEntity<GenericError__DTO> basicExceptionProcess(Exception exception) {
//	  log.error( exception.getMessage() );
	  log.error( "Exception" );
//	  return new ResponseEntity<>(new GenericError__DTO(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
	  return new ResponseEntity<>(new GenericError__DTO("Exception",HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
  }
  
//  @ExceptionHandler
//  public ResponseEntity<GenericError__DTO> basicExceptionProcess(Exception exception) {
//    log.error( exception.getMessage() );
//    return new ResponseEntity<>(new GenericError__DTO(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
//  }
}
