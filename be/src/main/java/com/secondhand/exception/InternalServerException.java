package com.secondhand.exception;

import com.secondhand.exception.v2.ErrorMessage;
import com.secondhand.exception.v2.SecondHandException;

public class InternalServerException extends SecondHandException {

    public InternalServerException(ErrorMessage errorCode) {
        super(errorCode);
    }

    public InternalServerException(ErrorMessage errorCode, String message) {
        super(errorCode, message);
    }
}
