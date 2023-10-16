package com.secondhand.exception;

import com.secondhand.exception.v2.ErrorMessage;
import com.secondhand.exception.v2.SecondHandException;

public class BadRequestException extends SecondHandException {
    public BadRequestException(ErrorMessage errorMessage) {
        super(errorMessage);
    }

    public BadRequestException(ErrorMessage errorMessage, String message) {
        super(errorMessage, message);
    }
}
