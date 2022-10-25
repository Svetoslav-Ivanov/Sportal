package com.example.sportal.model.exception;

public class UserNotLoggedException extends RuntimeException {
    public UserNotLoggedException(String msg) {
        super(msg);
    }
}
