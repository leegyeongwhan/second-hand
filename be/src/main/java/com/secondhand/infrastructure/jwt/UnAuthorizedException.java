package com.secondhand.infrastructure.jwt;

import com.secondhand.exception.ErrorMessage;
import com.secondhand.exception.SecondHandException;

public class UnAuthorizedException extends SecondHandException {

    public UnAuthorizedException(ErrorMessage errorCode) {
        super(errorCode);
    }

    public UnAuthorizedException(ErrorMessage errorCode, String message) {
        super(errorCode, message);
    }
}
