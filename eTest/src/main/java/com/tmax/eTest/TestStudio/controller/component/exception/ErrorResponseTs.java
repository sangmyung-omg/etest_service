package com.tmax.eTest.TestStudio.controller.component.exception;


import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponseTs {
  
  private final LocalDateTime timestamp = LocalDateTime.now();
  private final int httpStatusCode;
  private final String httpStatusName;
  private final String errorCode;
  private final String message;
  private final String isSuccess;

  public static ResponseEntity<ErrorResponseTs> toResponseEntity(ErrorCodeEnumTs errorCodeEnum) {
      return ResponseEntity
              .status(errorCodeEnum.getHttpStatus())
              .body(ErrorResponseTs.builder()
                      .httpStatusCode(errorCodeEnum.getHttpStatus().value())
                      .httpStatusName(errorCodeEnum.getHttpStatus().name())
                      .errorCode(errorCodeEnum.name())
                      .message(errorCodeEnum.getDetail())
                      .isSuccess("fail")
                      .build()
              );
  }
  
  
}
