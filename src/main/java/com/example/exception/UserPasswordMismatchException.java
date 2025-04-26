package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UserPasswordMismatchException extends RuntimeException {
    public UserPasswordMismatchException(String message) {
        super(message);
    } 
}