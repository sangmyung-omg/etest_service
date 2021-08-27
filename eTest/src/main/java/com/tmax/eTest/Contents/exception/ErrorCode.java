package com.tmax.eTest.Contents.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

public enum ErrorCode implements ErrorCodeBase {

  GENERIC_ERROR("0001", "Generic error has occured."), DB_ERROR("0002", "DB error has occured."),
  TYPE_ERROR("0003", "Query type is wrong.");

  static final String ERROR_CODE_PREFIX = "ERR-REC-";

  @Getter
  private String errorCode;
  @Getter
  private String message;
  @Getter
  private HttpStatus status;

  private ErrorCode(String errorCode, String message) {
    this.errorCode = ERROR_CODE_PREFIX + errorCode;
    this.message = message;
    this.status = HttpStatus.INTERNAL_SERVER_ERROR;
  }

  private ErrorCode(String errorCode, String message, HttpStatus status) {
    this.errorCode = ERROR_CODE_PREFIX + errorCode;
    this.message = message;
    this.status = status;
  }
}
