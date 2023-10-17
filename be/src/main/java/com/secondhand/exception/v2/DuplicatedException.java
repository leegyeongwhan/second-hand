package com.secondhand.exception.v2;

public class DuplicatedException extends SecondHandException {

    public DuplicatedException(ErrorMessage errorCode) {
        super(errorCode);
    }
}

