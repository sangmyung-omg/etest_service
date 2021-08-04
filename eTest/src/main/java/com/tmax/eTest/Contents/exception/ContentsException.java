package com.tmax.eTest.Contents.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import lombok.Getter;

@Getter
public class ContentsException extends ResponseStatusException {

  private String errorCode;
  private String message;

  public ContentsException(String errorCode, String message) {
    super(HttpStatus.INTERNAL_SERVER_ERROR);

    this.errorCode = errorCode;
    this.message = message;
  }

  public ContentsException(ErrorCodeBase error) {
    super(error.getStatus());

    this.errorCode = error.getErrorCode();
    this.message = error.getMessage();
  }

  public ContentsException(ErrorCodeBase error, String appendMessage) {
    super(error.getStatus());

    this.errorCode = error.getErrorCode();
    this.message = error.getMessage() + " | " + appendMessage;
  }

}
