package com.withus.be.common.auth.oauth.controller;

import com.withus.be.common.auth.oauth.GoogleOAuth;
import com.withus.be.common.auth.oauth.OAuthService;
import com.withus.be.common.auth.oauth.dto.GoogleOAuthToken;
import com.withus.be.domain.constant.Provider;
import com.withus.be.dto.TokenDto.OauthRefreshDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class OAuthController {
    private final GoogleOAuth googleOauth;
    private final OAuthService oAuthService;

    @GetMapping("/{type}")
    public void socialLoginRedirect(@PathVariable(name = "type") String type) throws IOException {
        Provider provider = Provider.valueOf(type.toUpperCase());
        oAuthService.request(provider);
    }

    @GetMapping(value = "/{type}/callback")
    public void callback(
            @PathVariable(name = "type") String type,
            @RequestParam(name = "code") String code,
            HttpServletRequest request
    ) throws Exception {
        log.info(">> 소셜 로그인 API 서버로부터 받은 code : {}", code);
        log.info("CODE : {}", code);

        GoogleOAuthToken googleOAuthToken = oAuthService.requestOAuthLogin(Provider.valueOf(type.toUpperCase()), code, request);
        String accessToken = googleOAuthToken.getAccess_token();

        // TODO response.sendRedirect 할건지 ResponseEntity로 값 넘겨줄건지
    }


    // TODO provider 별 분기처리 뒤에서 할 수 있도록 개선 필요
    @GetMapping(value = "/refresh")
    public void refresh(@RequestBody OauthRefreshDto refresh) {
        googleOauth.getRefreshToken(refresh.refreshToken());
    }

}
