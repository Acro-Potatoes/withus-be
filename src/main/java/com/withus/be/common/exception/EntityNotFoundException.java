package com.withus.be.common.exception;

import static com.withus.be.common.response.Result.NOT_FOUND;

public class EntityNotFoundException extends BaseException {

    public EntityNotFoundException() {
        super(NOT_FOUND);
    }

    public EntityNotFoundException(String message) {
        super(NOT_FOUND, message);
    }
}
