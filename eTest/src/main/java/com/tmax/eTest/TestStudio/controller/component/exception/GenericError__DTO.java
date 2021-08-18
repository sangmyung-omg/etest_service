package com.tmax.eTest.TestStudio.controller.component.exception;

import lombok.Data;

@Data
public class GenericError__DTO {
  private String httpStatusValue;
  private String message;

  public GenericError__DTO(String message) {
    this.message = message;
  }

  public GenericError__DTO(String httpStatusValue, String message) {
    this.httpStatusValue = httpStatusValue;
    this.message = message;
  }
}
