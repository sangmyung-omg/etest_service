package com.tmax.eTest.TestStudio.controller.component.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomExceptionTs extends RuntimeException {

  private final ErrorCodeTs errorCode;
  
}
