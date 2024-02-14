package com.withus.be.common.response;

import lombok.Getter;

@Getter
public enum Result {

    SUCCESS("0000", "success"),

    BAD_REQUEST("1000", "bad request"),
    NOT_FOUND("1001", "requested resource is not found."),
    CONFLICT("1002", "requested resource is a duplicate."),
    FORBIDDEN("1003", "this request is prohibited."),
    UNSUPPORTED("1004", "this provider is not supported."),
    INVALID("1005", "this request is invalid."),
    UN_AUTHORIZED("1006", "not authenticated."),

    INTERNAL_ERROR("9000", "internal error"),
    DATA_ACCESS_ERROR("9001", "data access error"),
    SYSTEM_ERROR("9002", "temporary error occurred."),
    CREDENTIAL_ERROR("9003", "credentials failed.");

    private final String code;
    private final String desc;

    Result(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


}
