package com.withus.be.common.exception;

import static com.withus.be.common.response.Result.UN_AUTHORIZED;

public class UnAuthenticationException extends BaseException {

    public UnAuthenticationException() {
        super(UN_AUTHORIZED);
    }

    public UnAuthenticationException(String message) {
        super(UN_AUTHORIZED, message);
    }
}
