package com.example.sportal.model.exception;

public class BadRequestException extends RuntimeException {

    public BadRequestException (String message){
        super(message);
    }

}
