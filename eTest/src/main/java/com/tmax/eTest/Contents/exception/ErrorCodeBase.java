package com.tmax.eTest.Contents.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCodeBase {
  public HttpStatus getStatus();

  public String getErrorCode();

  public String getMessage();

}
