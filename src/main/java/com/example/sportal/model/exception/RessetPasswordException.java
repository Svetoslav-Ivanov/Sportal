package com.example.sportal.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.OK)
public class RessetPasswordException extends RuntimeException {
    public RessetPasswordException(String message) {
        super(message);
    }
}
