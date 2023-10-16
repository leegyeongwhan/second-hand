package com.secondhand.exception.v2;

public class UnAuthorizedException extends SecondHandException {

    public UnAuthorizedException(ErrorMessage errorCode) {
        super(errorCode);
    }

    public UnAuthorizedException(ErrorMessage errorCode, String message) {
        super(errorCode, message);
    }
}
