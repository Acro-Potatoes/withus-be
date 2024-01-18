package com.withus.be.common.response;


public class ResponseFail extends Response {

    private String RESULT_CODE;
    private String RESULT_MESSAGE;
    private String ERROR_MESSAGE;

    public ResponseFail() {
    }

    public ResponseFail(Result result) {
        RESULT_CODE = result.getCode();
        RESULT_MESSAGE = result.getDesc();
    }

    public ResponseFail(Result result, String errMsg) {
        RESULT_CODE = result.getCode();
        RESULT_MESSAGE = result.getDesc();
        ERROR_MESSAGE = errMsg;
    }

    @Override
    protected String resultCode() {
        return RESULT_CODE;
    }

    @Override
    protected String resultMessage() {
        if (ERROR_MESSAGE == null || ERROR_MESSAGE.isEmpty()) return RESULT_MESSAGE;
        return RESULT_MESSAGE + "(" + ERROR_MESSAGE + ")";
    }

}
