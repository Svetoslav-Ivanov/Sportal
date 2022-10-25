package com.example.sportal.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.remoting.RemoteTimeoutException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
public class InvalidOperationException extends RuntimeException {
    public InvalidOperationException(String msg) {
        super(msg);
    }
}
