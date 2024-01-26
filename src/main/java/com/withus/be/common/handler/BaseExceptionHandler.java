package com.withus.be.common.handler;

import com.withus.be.common.exception.BaseException;
import com.withus.be.common.response.Response.Body;
import com.withus.be.common.response.ResponseFail;
import com.withus.be.common.response.Result;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.withus.be.common.interceptor.HttpRequestInterceptor.REQUEST_UUID;
import static com.withus.be.common.response.Result.BAD_REQUEST;
import static com.withus.be.common.response.Result.INTERNAL_ERROR;

@Slf4j
@ControllerAdvice
public class BaseExceptionHandler {

    /**
     * http status: 500 AND result: FAIL
     * <p>
     * 시스템 예외 상황
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Body> onException(Exception e, HttpServletResponse response) {
        String eventId = MDC.get(REQUEST_UUID);
        log.error("[Exception] eventId : {}", eventId, e);
        return new ResponseFail(httpStatusVerification(response)).fail();
    }

    /**
     * http status: 200 AND result: FAIL
     * <p>
     * 시스템은 이슈 없고, 비즈니스 로직 처리에서 에러 발생
     */
    @ResponseBody
    @ExceptionHandler(value = BaseException.class)
    public ResponseEntity<Body> onBaseException(BaseException e, HttpServletResponse response) {
        String eventId = MDC.get(REQUEST_UUID);
        String errMsg = NestedExceptionUtils.getMostSpecificCause(e).getMessage();
        log.error("[BaseException] eventId = {}, cause = {}, errMsg = {}",
                eventId,
                NestedExceptionUtils.getMostSpecificCause(e).getStackTrace()[0],
                errMsg);

        return new ResponseFail(httpStatusVerification(response), errMsg).fail();
    }

    // TODO : 임시 -> custom exception 
    @ResponseBody
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<Body> illegal(Exception e, HttpServletResponse response) {
        String eventId = MDC.get(REQUEST_UUID);
        String errMsg = NestedExceptionUtils.getMostSpecificCause(e).getMessage();
        log.error("[BaseException] eventId = {}, cause = {}, errMsg = {}",
                eventId,
                NestedExceptionUtils.getMostSpecificCause(e).getStackTrace()[0],
                errMsg);

        return new ResponseFail(httpStatusVerification(response), errMsg).fail();
    }

    // TODO 수정 필요 - 현재 너무 한정적임
    private Result httpStatusVerification(HttpServletResponse response) {
        HttpStatus httpStatus = HttpStatus.valueOf(response.getStatus());
        return httpStatus.is4xxClientError() ? BAD_REQUEST : INTERNAL_ERROR;
    }
}
