package com.tmax.eTest.TestStudio.controller.component.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class GenericError__DTO {
	
  private LocalDateTime timestamp = LocalDateTime.now();
  private int httpStatusCode;
  private String httpStatusName;
  private String errorCode;
  private String message;
  private String isSuccess;

  public GenericError__DTO(String message, HttpStatus httpStatus) {
    this.message = message;
    this.httpStatusCode = httpStatus.value();
    this.httpStatusName = httpStatus.name();
    this.isSuccess = "fail";
  }

  public GenericError__DTO(String errorCode, String message, HttpStatus httpStatus) {
    this.errorCode = errorCode;
    this.message = message;
    this.httpStatusCode = httpStatus.value();
    this.httpStatusName = httpStatus.name();
    this.isSuccess = "fail";
  }
  
//  public GenericError__DTO(String errorCode, String message, String isSuccess, HttpStatus httpStatus) {
//	    this.errorCode = errorCode;
//	    this.message = message;
//	    this.httpStatusCode = httpStatus.value();
//	    this.httpStatusName = httpStatus.name();
//	    this.isSuccess = isSuccess;
//	  }
}
