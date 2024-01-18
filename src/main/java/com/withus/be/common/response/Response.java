package com.withus.be.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public abstract class Response {

    public record Body(String code, String message, Object data) {}

    /**
     * <p> 메세지만 가진 성공 응답을 반환한다.</p>
     * <pre>
     *     {
     *         "code" : 0000,
     *         "message" : success,
     *         "data" : []
     *     }
     * </pre>
     */
    public ResponseEntity<Body> success() {
        return new ResponseEntity<>(getBody(Optional.empty()), HttpStatus.OK);
    }

    /**
     * <p> 메세지와 데이터를 포함한 성공 응답을 반환한다.</p>
     * <pre>
     *     {
     *         "code" : 0000,
     *         "message" : success,
     *         "data" : [{data1}, {data2}...]
     *     }
     * </pre>
     */
    public ResponseEntity<Body> success(Object data) {
        return new ResponseEntity<>(getBody(data), HttpStatus.OK);
    }

    /**
     * <p> 메세지만 가진 실패 응답을 반환한다.</p>
     * <pre>
     *     {
     *         "code" : 9000,
     *         "message" : system error,
     *         "data" : []
     *     }
     * </pre>
     */
    public ResponseEntity<Body> fail() {
        return new ResponseEntity<>(getBody(Optional.empty()), HttpStatus.OK);
    }

    private Body getBody(Object data) {
        return new Body(resultCode(), resultMessage(), data);
    }

    protected abstract String resultCode();

    protected abstract String resultMessage();

}
