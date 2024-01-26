package com.withus.be.common.auth.oauth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.withus.be.common.auth.oauth.OAuthGoogleService;
import com.withus.be.common.auth.oauth.OAuthTokenService;
import com.withus.be.common.response.Response.Body;
import com.withus.be.common.response.ResponseSuccess;
import com.withus.be.domain.constant.Provider;
import com.withus.be.dto.TokenDto.OauthRefDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.withus.be.common.auth.oauth.OAuthOption.AUTHORIZATION_HEADER;
import static com.withus.be.common.auth.oauth.OAuthOption.JWT_HEADER_PREFIX;

@Tag(name = "OAuth Controller", description = "소셜 인증 관련 컨트롤러")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class OAuthController {

    private final OAuthGoogleService oauthGoogleService;
    private final OAuthTokenService oAuthTokenService;

    @Operation(summary = "소설 로그인 API")
    @GetMapping("/{type}")
    public void socialLoginRedirect(@PathVariable(name = "type") String type, HttpServletResponse response) throws IOException {
        oAuthTokenService.providerValidCheck(getProvider(type));
        response.sendRedirect(oauthGoogleService.getOAuthRedirectURL());
    }

    @Operation(summary = "소설 로그인 Callback API", description = "소셜 로그인 API 호출해서 요청 성공시 자동으로 호출")
    @GetMapping(value = "/{type}/callback")
    public ResponseEntity<Body> callback(
            @PathVariable(name = "type") String type,
            @RequestParam(name = "code") String code
    ) throws Exception {
        log.info("소셜 로그인 API 로부터 받은 code : {}", code);

        oAuthTokenService.providerValidCheck(getProvider(type));

        String accessToken = oAuthTokenService.requestOAuthLogin(code);
        return getTokenRes(accessToken);
    }

    @Operation(summary = "소설 로그인 토큰 재발급 API", description = "Refresh Token으로 Access Token 재발급")
    @GetMapping(value = "/{type}/refresh")
    public ResponseEntity<Body> refresh(
            @PathVariable("type") String type,
            @RequestBody OauthRefDto refresh
    ) throws JsonProcessingException {
        String responseBody = oauthGoogleService.requestRefreshToken(getProvider(type), refresh.refreshToken());
        String accessToken = oAuthTokenService.requestToken(responseBody);
        return getTokenRes(accessToken);
    }

    private ResponseEntity<Body> getTokenRes(String accessToken) {
        new HttpHeaders().add(AUTHORIZATION_HEADER, JWT_HEADER_PREFIX + accessToken);
        return new ResponseSuccess().success(JWT_HEADER_PREFIX + accessToken);
    }

    private Provider getProvider(String type) {
        return Provider.valueOf(type.toUpperCase());
    }

}
