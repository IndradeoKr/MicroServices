package com.employee.exception;

import org.springframework.http.HttpStatus;

public class MissingParameterException extends RuntimeException{
    private String message;
    private HttpStatus status;

    public MissingParameterException(String message) {
        this.message = message;
        this.status = HttpStatus.BAD_REQUEST;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
