package com.withus.be.common.auth.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.withus.be.common.auth.oauth.dto.GoogleOAuthToken;
import com.withus.be.common.auth.oauth.dto.GoogleUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

        Map<String, Object> params = new HashMap<>();
        params.put("scope", GOOGLE_DATA_ACCESS_SCOPE);
        params.put("response_type", "code");
        params.put("client_id", GOOGLE_SNS_CLIENT_ID);
        params.put("redirect_uri", GOOGLE_SNS_REDIRECT_URL);
        params.put("access_type", "offline");

        //parameter를 형식에 맞춰 구성해주는 함수
        String parameterString = params.entrySet().stream()
                .map(x -> x.getKey() + "=" + x.getValue())
                .collect(Collectors.joining("&"));
        String redirectURL = GOOGLE_SNS_LOGIN_URL + "?" + parameterString;
        System.out.println("redirectURL = " + redirectURL);

        return redirectURL;
    }

    public ResponseEntity<String> requestAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", GOOGLE_SNS_CLIENT_ID);
        params.put("client_secret", GOOGLE_SNS_CLIENT_SECRET);
        params.put("redirect_uri", GOOGLE_SNS_REDIRECT_URL);
        params.put("grant_type", "authorization_code");

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(GOOGLE_SNS_TOKEN_BASE_URL, params, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) return responseEntity;
        return null;
    }

    public ResponseEntity<String> getRefreshToken(String refreshToken) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> params = new HashMap<>();
        params.put("client_id", GOOGLE_SNS_CLIENT_ID);
        params.put("client_secret", GOOGLE_SNS_CLIENT_SECRET);
        params.put("grant_type", "refresh_token");
        params.put("refresh_token", refreshToken);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(GOOGLE_SNS_TOKEN_BASE_URL, params, String.class);

        if (responseEntity.getStatusCode() != HttpStatus.OK) return null;
        return responseEntity;
    }

    public GoogleOAuthToken getAccessToken(ResponseEntity<String> response) throws JsonProcessingException {
        log.info("GoogleOAuth response.getBody() :: " + response.getBody());
        return new ObjectMapper().readValue(response.getBody(), GoogleOAuthToken.class);
    }

    /**
     * 구글로 엑세스 토큰을 보내 구글 사용자 정보를 받아온다.
     *
     * @param oAuthToken
     * @return
     */
    public ResponseEntity<String> requestUserInfo(GoogleOAuthToken oAuthToken) {

        // header에 accessToken을 담는다.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + oAuthToken.getAccess_token());

        //HttpEntity를 하나 생성해 헤더를 담아서 restTemplate으로 구글과 통신하게 한다.
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.exchange(GOOGLE_SNS_USERINFO_REQUEST_URL, HttpMethod.GET, request, String.class);
    }

    public GoogleUser getUserInfo(ResponseEntity<String> userInfoRes) throws JsonProcessingException {
        return new ObjectMapper().readValue(userInfoRes.getBody(), GoogleUser.class);
    }
}
