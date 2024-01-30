package com.withus.be.common.exception;

import static com.withus.be.common.response.Result.INVALID;

public class InvalidTokenException extends BaseException {

    public InvalidTokenException() {
        super(INVALID);
    }

    public InvalidTokenException(String message) {
        super(INVALID, message);
    }
}
