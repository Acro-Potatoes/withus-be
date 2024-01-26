package com.withus.be.common.handler;

import com.withus.be.common.exception.BaseException;
import com.withus.be.common.response.Response.Body;
import com.withus.be.common.response.ResponseFail;
import com.withus.be.common.response.ResponseSuccess;
import com.withus.be.common.response.Result;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.slf4j.MDC;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.withus.be.common.interceptor.HttpRequestInterceptor.REQUEST_UUID;
import static com.withus.be.common.response.Result.*;

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
    public ResponseEntity<Body> onException(Exception e) {
        String eventId = MDC.get(REQUEST_UUID);
        log.error("[Exception] eventId : {}", eventId, e);
        return new ResponseFail(INTERNAL_ERROR).fail();
    }

    /**
     * http status: 200 AND result: FAIL
     * <p>
     * 시스템은 이슈 없고, 비즈니스 로직 처리에서 에러 발생
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = BaseException.class)
    public ResponseEntity<Body> onBaseException(BaseException e) {
        String eventId = MDC.get(REQUEST_UUID);
        String errMsg = NestedExceptionUtils.getMostSpecificCause(e).getMessage();
        log.error("[BaseException] eventId = {}, cause = {}, errMsg = {}",
                eventId,
                NestedExceptionUtils.getMostSpecificCause(e).getStackTrace()[0],
                errMsg);

        return new ResponseFail(e.getResult(), errMsg).fail();
    }

    /**
     * 예상치 않은 Exception 중에서 모니터링 skip 이 가능한 Exception 을 처리할 때
     * <p>
     * ex) ClientAbortException
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = {ClientAbortException.class})
    public ResponseEntity<Body> skipException(Exception e) {
        String eventId = MDC.get(REQUEST_UUID);
        String errMsg = NestedExceptionUtils.getMostSpecificCause(e).getMessage();
        log.warn("[skipException] eventId = {}, cause = {}, errorMsg = {}",
                eventId,
                NestedExceptionUtils.getMostSpecificCause(e),
                errMsg);

        return new ResponseFail(SYSTEM_ERROR, errMsg).fail();
    }

    /**
     * http status: 400 AND result: FAIL
     * request parameter 에러
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Body> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        String eventId = MDC.get(REQUEST_UUID);
        String errMsg = NestedExceptionUtils.getMostSpecificCause(e).getMessage();
        log.warn("[BaseException] eventId = {}, errorMsg = {}", eventId, errMsg);

        BindingResult bindingResult = e.getBindingResult();
        FieldError fe = bindingResult.getFieldError();
        if (fe != null) {
            String message = "Request Error" + " " + fe.getField() + "=" + fe.getRejectedValue() + " (" + fe.getDefaultMessage() + ")";
            return new ResponseFail(INVALID, message).fail();
        } else {
            return new ResponseFail(INVALID).fail();
        }
    }

}
