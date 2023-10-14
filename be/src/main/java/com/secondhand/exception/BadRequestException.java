package com.secondhand.exception;

import com.secondhand.exception.ErrorMessage;

public class BadRequestException extends SecondHandException {
    public BadRequestException(ErrorMessage errorMessage) {
        super(errorMessage);
    }

    public BadRequestException(ErrorMessage errorMessage, String message) {
        super(errorMessage, message);
    }
}
