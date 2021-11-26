package com.tmax.eTest.Contents.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

public enum ErrorCode implements ErrorCodeBase {

  GENERIC_ERROR("0001", "Generic error has occured.", HttpStatus.CONFLICT),
  DB_ERROR("0002", "DB error has occured.", HttpStatus.CONFLICT),
  TYPE_ERROR("0003", "Query type is wrong.", HttpStatus.BAD_REQUEST),
  LRS_ERROR("0004", "LRS Service error has occured.", HttpStatus.CONFLICT),
  DIAGNOSIS_ERROR("0005", "Diagnosis Report Error has occured.", HttpStatus.CONFLICT),
  USER_ERROR("0006", "User Should not be null.", HttpStatus.UNAUTHORIZED),
  FILE_ERROR("0007", "File has Error.", HttpStatus.BAD_REQUEST),
  CODE_ERROR("0008", "Code has Error.", HttpStatus.CONFLICT);

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
