package com.withus.be.common.exception;

import static com.withus.be.common.response.Result.CONFLICT;

public class DuplicatedException extends BaseException {
    public  DuplicatedException() {
        super(CONFLICT);
    }

    public  DuplicatedException(String message) {
        super(CONFLICT, message);
    }
}

