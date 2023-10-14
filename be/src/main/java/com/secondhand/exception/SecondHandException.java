package com.secondhand.exception;

import lombok.Getter;

@Getter
public class SecondHandException extends RuntimeException {

    private final ErrorMessage errorMessage;
    private final String message;

    public SecondHandException(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
        this.message = errorMessage.getMessage();
    }

    public SecondHandException(ErrorMessage errorMessage, String message) {
        this.errorMessage = errorMessage;
        this.message = message;
    }
}
