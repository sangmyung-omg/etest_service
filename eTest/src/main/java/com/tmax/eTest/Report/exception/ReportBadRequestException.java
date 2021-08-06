package com.tmax.eTest.Report.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ReportBadRequestException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public ReportBadRequestException(String message) {
        super(message);
    }

    public ReportBadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
