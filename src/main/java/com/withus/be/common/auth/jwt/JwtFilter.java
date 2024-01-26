package com.withus.be.common.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

import static com.withus.be.common.auth.oauth.OAuthOption.AUTHORIZATION_HEADER;
import static com.withus.be.common.auth.oauth.OAuthOption.JWT_HEADER_PREFIX;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private final JwtTokenValidator jwtTokenValidator;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = resolveToken(httpServletRequest);
        String requestUri = httpServletRequest.getRequestURI();

        if (!StringUtils.hasText(token) || !jwtTokenValidator.validateToken(token))
            log.debug("유효한 JWT 토큰이 없습니다, uri : {}", requestUri);

        Authentication authentication = jwtTokenValidator.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri : {}", authentication.getName(), requestUri);

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (!StringUtils.hasText(bearerToken) || !bearerToken.startsWith(JWT_HEADER_PREFIX)) return null;
        return bearerToken.substring(7);
    }
}
