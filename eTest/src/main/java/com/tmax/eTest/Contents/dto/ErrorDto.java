package com.tmax.eTest.Contents.dto;

import lombok.Data;

@Data
public class ErrorDto {
  private String errorCode;
  private String message;

  public ErrorDto(String message) {
    this.message = message;
  }

  public ErrorDto(String errorCode, String message){
    this.errorCode=errorCode;
    this.message=message;
  }
}
