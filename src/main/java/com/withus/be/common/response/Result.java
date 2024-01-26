package com.withus.be.common.response;

import lombok.Getter;

@Getter
public enum Result {

    SUCCESS("0000", "success"),

    BAD_REQUEST("1000", "bad request"),
    NOT_FOUND("1001", "requested resource is not found"),
    CONFLICT("1002", "requested resource is a duplicate"),
    FORBIDDEN("1003", "This request is prohibited"),


    INTERNAL_ERROR("9000", "internal error"),
    DATA_ACCESS_ERROR("9001", "data access error");

    private final String code;
    private final String desc;

    Result(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


}
