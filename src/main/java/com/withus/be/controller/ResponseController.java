package com.withus.be.controller;

import com.withus.be.common.response.Response.Body;
import com.withus.be.common.response.ResponseFail;
import com.withus.be.common.response.ResponseSuccess;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 공통 response 사용 예제 컨트롤러
 */
@RestController
public class ResponseController {

    @GetMapping("/success")
    public ResponseEntity<Body> getSuccess() {
        return new ResponseSuccess().success(
                Stream.of("Java", "Spring", "JPA")
                        .collect(Collectors.toList()));
    }

    @GetMapping("/fail")
    public ResponseEntity<Body> getFail() {
        return new ResponseFail().fail();
    }
}
