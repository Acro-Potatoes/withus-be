package com.withus.be.common.response;

import static com.withus.be.common.response.Result.SUCCESS;

public class ResponseSuccess extends Response {

    private static final String DEFAULT_CODE = SUCCESS.getCode();
    private static final String DEFAULT_MESSAGE = SUCCESS.getDesc();
    private String message;

    public ResponseSuccess() {
    }

    public ResponseSuccess(String message) {
        this.message = message;
    }

    @Override
    protected String resultCode() {
        return DEFAULT_CODE;
    }

    @Override
    protected String resultMessage() {
        return message == null ? DEFAULT_MESSAGE : message;
    }

}
