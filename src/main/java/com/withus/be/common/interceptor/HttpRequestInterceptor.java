package com.withus.be.common.interceptor;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Slf4j
@Component
public class HttpRequestInterceptor implements HandlerInterceptor {

    public static final String REQUEST_UUID = "x-request-id";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestId = request.getHeader(REQUEST_UUID);
        if (StringUtils.isEmpty(requestId)) requestId = String.valueOf(UUID.randomUUID());

        MDC.put(REQUEST_UUID, requestId);
        return true;
    }
}
