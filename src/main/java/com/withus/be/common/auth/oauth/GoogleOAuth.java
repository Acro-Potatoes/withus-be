package com.withus.be.common.auth.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.withus.be.common.auth.oauth.dto.GoogleOAuthToken;
import com.withus.be.common.exception.UnknownProviderException;
import com.withus.be.domain.constant.Provider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.withus.be.common.auth.oauth.OAuthOption.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleOAuth {

    @Value("${OAuth2.google.url}")
    private String GOOGLE_SNS_LOGIN_URL;
    @Value("${OAuth2.google.client-id}")
    private String GOOGLE_SNS_CLIENT_ID;
    @Value("${OAuth2.google.redirect-url}")
    private String GOOGLE_SNS_REDIRECT_URL;
    @Value("${OAuth2.google.client-secret}")
    private String GOOGLE_SNS_CLIENT_SECRET;
    @Value("${OAuth2.google.scope}")
    private String GOOGLE_DATA_ACCESS_SCOPE;
    @Value("${OAuth2.google.userinfo-request-url}")
    private String GOOGLE_SNS_USERINFO_REQUEST_URL;
    @Value("${OAuth2.google.token-base-url}")
    private String GOOGLE_SNS_TOKEN_BASE_URL;

    public String getOAuthRedirectURL() {
        Map<String, Object> params = ImmutableMap.<String, Object>builder()
                .put(SCOPE, GOOGLE_DATA_ACCESS_SCOPE)
                .put(RESPONSE_TYPE, CODE)
                .put(CLIENT_ID, GOOGLE_SNS_CLIENT_ID)
                .put(REDIRECT_URI, GOOGLE_SNS_REDIRECT_URL)
                .put(ACCESS_TYPE, OFFLINE)
                .build();

        return getRedirectURL(params);
    }

    private String getRedirectURL(Map<String, Object> params) {
        String parameterString = params.entrySet().stream()
                .map(x -> x.getKey() + "=" + x.getValue())
                .collect(Collectors.joining("&"));

        String redirectURL = GOOGLE_SNS_LOGIN_URL + "?" + parameterString;
        log.info("redirectURL : {}", redirectURL);
        return redirectURL;
    }

    public String requestAccessToken(String code) {
        Map<String, Object> params = ImmutableMap.<String, Object>builder()
                .put(CODE, code)
                .put(CLIENT_ID, GOOGLE_SNS_CLIENT_ID)
                .put(CLIENT_SECRET, GOOGLE_SNS_CLIENT_SECRET)
                .put(REDIRECT_URI, GOOGLE_SNS_REDIRECT_URL)
                .put(GRANT_TYPE, AUTHORIZATION_CODE)
                .build();

        return getTokenResBody(new RestTemplate(), params);
    }

    public String requestRefreshToken(Provider provider, String refreshToken) {
        if (Objects.requireNonNull(provider) != Provider.GOOGLE)
            throw new UnknownProviderException("알 수 없는 소셜 로그인 형식입니다.");

        Map<String, Object> params = ImmutableMap.<String, Object>builder()
                .put(CLIENT_ID, GOOGLE_SNS_CLIENT_ID)
                .put(CLIENT_SECRET, GOOGLE_SNS_CLIENT_SECRET)
                .put(GRANT_TYPE, REFRESH_TOKEN)
                .put(REFRESH_TOKEN, refreshToken)
                .build();

        return getTokenResBody(new RestTemplate(), params);
    }

    private String getTokenResBody(RestTemplate restTemplate, Map<String, Object> params) {
        ResponseEntity<String> tokenRes = restTemplate.postForEntity(GOOGLE_SNS_TOKEN_BASE_URL, params, String.class);
        return tokenRes.getStatusCode() == HttpStatus.OK ? tokenRes.getBody() : "";
    }

    public GoogleOAuthToken getAccessToken(String responseBody) throws JsonProcessingException {
        log.info("GoogleOAuth response.getBody() :: " + responseBody);
        return new ObjectMapper().readValue(responseBody, GoogleOAuthToken.class);
    }

    /**
     * 구글로 엑세스 토큰을 보내 구글 사용자 정보를 받아온다.
     *
     * @param oAuthToken
     * @return
     */
    public String requestUserInfo(GoogleOAuthToken oAuthToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION_HEADER, JWT_HEADER_PREFIX + oAuthToken.getAccess_token());

        //HttpEntity를 하나 생성해 헤더를 담아 restTemplate으로 구글과 통신
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = new RestTemplate()
                .exchange(GOOGLE_SNS_USERINFO_REQUEST_URL, HttpMethod.GET, request, String.class);
        return response.getStatusCode().equals(HttpStatus.OK) ? response.getBody() : "";
    }

}
