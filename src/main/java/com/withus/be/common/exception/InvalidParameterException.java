package com.withus.be.common.exception;

import static com.withus.be.common.response.Result.INVALID;

public class InvalidParameterException extends BaseException {

    public InvalidParameterException() {
        super(INVALID);
    }

    public InvalidParameterException(String message) {
        super(INVALID, message);
    }
}
