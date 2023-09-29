package com.myblog11.exception;

import org.springframework.http.HttpStatus;

public class BlogApi extends RuntimeException{

    private HttpStatus status;
    private String message;
    public BlogApi(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
    public BlogApi(String message, HttpStatus status, String message1) {
        super(message);
        this.status = status;
        this.message = message1;
    }
    public HttpStatus getStatus() {
        return status;
    }
    @Override
    public String getMessage() {
        return message;
    }

}
