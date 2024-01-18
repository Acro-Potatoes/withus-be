package com.withus.be.common.exception;

import com.withus.be.common.response.Result;
import lombok.Getter;

/**
 * BaseException 또는 BaseException 을 확장한 Exception 은
 * 서비스 운영에서 예상이 가능한 Exception 을 표현한다.
 * <p>
 * http status: 200 이면서 result: FAIL
 */
@Getter
public class BaseException extends RuntimeException {

    private Result result;

    public BaseException() {

    }

    public BaseException(Result result) {
        super(result.getDesc());
        this.result = result;
    }

    public BaseException(Result result, String message) {
        super(message);
        this.result = result;
    }

    public BaseException(Result result, String message, Throwable cause) {
        super(message, cause);
        this.result = result;
    }

}
