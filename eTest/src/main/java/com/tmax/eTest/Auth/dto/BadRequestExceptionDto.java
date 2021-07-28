package com.tmax.eTest.Auth.dto;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestExceptionDto extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BadRequestExceptionDto(String message) { super(message); }

    public BadRequestExceptionDto(String message, Throwable cause) { super(message, cause); }
}