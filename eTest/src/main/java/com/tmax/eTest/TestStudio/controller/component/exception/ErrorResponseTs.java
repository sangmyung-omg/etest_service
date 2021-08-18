package com.tmax.eTest.TestStudio.controller.component.exception;


import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponseTs {
  
  private final LocalDateTime timestamp = LocalDateTime.now();
  private final int httpStatusValue;
  private final String httpStatusName;
  private final String code;
  private final String message;

  public static ResponseEntity<ErrorResponseTs> toResponseEntity(ErrorCodeTs errorCode) {
      return ResponseEntity
              .status(errorCode.getHttpStatus())
              .body(ErrorResponseTs.builder()
                      .httpStatusValue(errorCode.getHttpStatus().value())
                      .httpStatusName(errorCode.getHttpStatus().name())
                      .code(errorCode.name())
                      .message(errorCode.getDetail())
                      .build()
              );
  }
  
  
}
