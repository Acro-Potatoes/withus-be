package com.withus.be.common.exception;

import static com.withus.be.common.response.Result.UNSUPPORTED;

public class UnknownProviderException extends BaseException {

    public UnknownProviderException() {
        super(UNSUPPORTED);
    }

    public UnknownProviderException(String message) {
        super(UNSUPPORTED, message);
    }
}
