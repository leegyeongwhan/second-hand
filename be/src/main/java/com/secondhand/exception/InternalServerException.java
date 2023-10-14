package com.secondhand.exception;

public class InternalServerException extends SecondHandException {

    public InternalServerException(ErrorMessage errorCode) {
        super(errorCode);
    }

    public InternalServerException(ErrorMessage errorCode, String message) {
        super(errorCode, message);
    }
}
