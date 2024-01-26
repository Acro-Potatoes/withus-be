package com.withus.be.controller;

import com.withus.be.common.auth.jwt.JwtFilter;
import com.withus.be.common.auth.jwt.TokenProvider;
import com.withus.be.dto.LoginDto;
import com.withus.be.dto.MemberDto;
import com.withus.be.dto.TokenDto;
import com.withus.be.dto.TokenDto.TokenResponse;
import com.withus.be.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth Controller", description = "인증 관련 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private static final String HEADER_PREFIX = "Bearer ";
    private final AuthService authService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Operation(summary = "회원가입 API")
    @PostMapping("/signup")
    public ResponseEntity<MemberDto.MemberResponse> signup(@Valid @RequestBody MemberDto.MemberRequest memberRequest) {
        return ResponseEntity.ok(authService.signup(memberRequest));
    }

    @Operation(summary = "로그인 API")
    @PostMapping("/login")
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenDto jwt = tokenProvider.generateToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, HEADER_PREFIX + jwt.getAccessToken());

        return new ResponseEntity<>(jwt, httpHeaders, HttpStatus.OK);
    }

    @Operation(summary = "Token 재발급", description = "Refresh Token으로 Token 재발급 API")
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> getRefresh(@RequestBody TokenDto tokenDto) {
        TokenResponse token = tokenProvider.generateTokenByRefreshToken(tokenDto.getAccessToken(), tokenDto.getRefreshToken());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, HEADER_PREFIX + token);

        return new ResponseEntity<>(token, httpHeaders, HttpStatus.OK);
    }

}
