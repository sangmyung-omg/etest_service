package com.tmax.eTest.Contents.dto;

import lombok.Data;

@Data
public class GenericErrorDTO {
  private String errorCode;
  private String message;

  public GenericErrorDTO(String message) {
    this.message = message;
  }

  public GenericErrorDTO(String errorCode, String message) {
    this.errorCode = errorCode;
    this.message = message;
  }
}
