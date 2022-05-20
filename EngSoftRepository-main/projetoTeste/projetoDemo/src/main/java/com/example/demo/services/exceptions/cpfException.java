package com.example.demo.services.exceptions;

public class cpfException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public cpfException(String message, Throwable cause) {
        super(message, cause);
    }

    public cpfException(String message) {
        super(message);
    }

}